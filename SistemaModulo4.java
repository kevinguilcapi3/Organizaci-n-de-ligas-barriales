import java.util.ArrayList;

public class SistemaModulo4 {
    public static final String JUVENIL = "Juvenil";
    public static final String ABIERTA = "Abierta";
    public static final String MASTER = "Máster";

    private ArrayList<Posicion> equipos = new ArrayList<Posicion>();
    private ArrayList<PartidoConsulta> partidos = new ArrayList<PartidoConsulta>();
    private String ultimoError = "";

    public ArrayList<Posicion> getEquipos() { return equipos; }
    public ArrayList<PartidoConsulta> getPartidos() { return partidos; }
    public String getUltimoError() { return ultimoError; }

    public boolean categoriaValida(String categoria) {
        return JUVENIL.equals(categoria) || ABIERTA.equals(categoria)
                || MASTER.equals(categoria);
    }

    private Posicion buscarEquipo(String nombre, String categoria) {
        if (nombre == null) return null;
        for (Posicion p : equipos) {
            if (p.getCategoria().equals(categoria)
                    && p.getEquipo().equalsIgnoreCase(nombre.trim())) {
                return p;
            }
        }
        return null;
    }

    public boolean agregarEquipo(String nombre, String categoria) {
        ultimoError = "";
        if (nombre == null || nombre.trim().isEmpty()) {
            ultimoError = "Debe escribir un nombre.";
            return false;
        }
        if (nombre.trim().length() > 30) {
            ultimoError = "El nombre debe tener máximo 30 caracteres.";
            return false;
        }
        if (!categoriaValida(categoria)) {
            ultimoError = "La categoría no es válida.";
            return false;
        }
        if (buscarEquipo(nombre, categoria) != null) {
            ultimoError = "Ese equipo ya está registrado en esta categoría.";
            return false;
        }
        equipos.add(new Posicion(nombre.trim(), categoria));
        return true;
    }

    // Comprueba formato y fecha real sin manejo de excepciones.
    public boolean fechaValida(String fecha) {
        if (fecha == null || fecha.length() != 10) return false;
        if (fecha.charAt(4) != '-' || fecha.charAt(7) != '-') return false;
        for (int i = 0; i < 10; i++) {
            if (i != 4 && i != 7) {
                if (fecha.charAt(i) < '0' || fecha.charAt(i) > '9') return false;
            }
        }
        int anio = Integer.parseInt(fecha.substring(0, 4));
        int mes = Integer.parseInt(fecha.substring(5, 7));
        int dia = Integer.parseInt(fecha.substring(8, 10));
        if (anio < 2020 || anio > 2200 || mes < 1 || mes > 12) return false;
        int maximo = 31;
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) maximo = 30;
        if (mes == 2) {
            maximo = 28;
            if (anio % 400 == 0 || (anio % 4 == 0 && anio % 100 != 0)) maximo = 29;
        }
        return dia >= 1 && dia <= maximo;
    }

    public PartidoConsulta agregarPartido(String categoria, String local,
                                          String visitante, String fecha) {
        ultimoError = "";
        if (!categoriaValida(categoria)) {
            ultimoError = "Seleccione una categoría válida.";
            return null;
        }
        if (!fechaValida(fecha)) {
            ultimoError = "La fecha es incorrecta. Use AAAA-MM-DD.";
            return null;
        }
        Posicion equipoLocal = buscarEquipo(local, categoria);
        Posicion equipoVisitante = buscarEquipo(visitante, categoria);
        if (equipoLocal == null || equipoVisitante == null) {
            ultimoError = "Registre ambos equipos en la categoría.";
            return null;
        }
        if (equipoLocal == equipoVisitante) {
            ultimoError = "Un equipo no puede enfrentarse a sí mismo.";
            return null;
        }
        for (PartidoConsulta p : partidos) {
            boolean mismosEquipos =
                    (p.getLocal().equals(equipoLocal.getEquipo())
                    && p.getVisitante().equals(equipoVisitante.getEquipo()))
                    || (p.getLocal().equals(equipoVisitante.getEquipo())
                    && p.getVisitante().equals(equipoLocal.getEquipo()));
            if (p.getCategoria().equals(categoria) && p.getFecha().equals(fecha)
                    && mismosEquipos) {
                ultimoError = "Ese partido ya está registrado en la fecha.";
                return null;
            }
        }
        PartidoConsulta nuevo = new PartidoConsulta(categoria,
                equipoLocal.getEquipo(), equipoVisitante.getEquipo(), fecha);
        partidos.add(nuevo);
        return nuevo;
    }

    public boolean registrarResultado(PartidoConsulta partido, int golesLocal,
                                      int golesVisitante) {
        ultimoError = "";
        if (partido == null || !partidos.contains(partido)) {
            ultimoError = "El partido no está registrado.";
            return false;
        }
        if (partido.estaFinalizado()) {
            ultimoError = "Este partido ya tiene un resultado.";
            return false;
        }
        if (golesLocal < 0 || golesVisitante < 0
                || golesLocal > 30 || golesVisitante > 30) {
            ultimoError = "Los goles deben estar entre 0 y 30.";
            return false;
        }
        Posicion local = buscarEquipo(partido.getLocal(), partido.getCategoria());
        Posicion visitante = buscarEquipo(partido.getVisitante(), partido.getCategoria());
        if (local == null || visitante == null) {
            ultimoError = "No se encontraron los equipos.";
            return false;
        }
        if (!partido.finalizar(golesLocal, golesVisitante)) {
            ultimoError = "El partido no se pudo finalizar.";
            return false;
        }
        local.sumarResultado(golesLocal, golesVisitante);
        visitante.sumarResultado(golesVisitante, golesLocal);
        return true;
    }

    public ArrayList<Posicion> tabla(String categoria) {
        ArrayList<Posicion> lista = new ArrayList<Posicion>();
        for (Posicion p : equipos) {
            if (p.getCategoria().equals(categoria)) lista.add(p);
        }
        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {
                Posicion a = lista.get(i);
                Posicion b = lista.get(j);
                boolean cambiar = false;
                if (b.getPuntos() > a.getPuntos()) cambiar = true;
                else if (b.getPuntos() == a.getPuntos()
                        && b.getDiferencia() > a.getDiferencia()) cambiar = true;
                else if (b.getPuntos() == a.getPuntos()
                        && b.getDiferencia() == a.getDiferencia()
                        && b.getGolesFavor() > a.getGolesFavor()) cambiar = true;
                if (cambiar) {
                    lista.set(i, b);
                    lista.set(j, a);
                }
            }
        }
        return lista;
    }

    public ArrayList<PartidoConsulta> consultar(String categoria, String equipo,
                                                String fecha) {
        ArrayList<PartidoConsulta> lista = new ArrayList<PartidoConsulta>();
        for (PartidoConsulta p : partidos) {
            boolean categoriaOk = p.getCategoria().equals(categoria);
            boolean equipoOk = "Todos".equals(equipo)
                    || p.getLocal().equals(equipo) || p.getVisitante().equals(equipo);
            boolean fechaOk = fecha == null || fecha.isEmpty()
                    || p.getFecha().equals(fecha);
            if (categoriaOk && equipoOk && fechaOk) lista.add(p);
        }
        return lista;
    }
}
