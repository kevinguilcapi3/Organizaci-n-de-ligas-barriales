import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner entrada = new Scanner(System.in);
    static ArrayList<Persona> personas = new ArrayList<Persona>();
    static ArrayList<Equipo> equipos = new ArrayList<Equipo>();
    static ArrayList<Partido> partidos = new ArrayList<Partido>();

    public static void main(String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("consola")) {
            javax.swing.SwingUtilities.invokeLater(() -> new VentanaLiga().setVisible(true));
            return;
        }
        int opcion;
        do {
            System.out.println("\n========== CAMPEONATO BARRIAL ==========");
            System.out.println("1. Registrar persona");
            System.out.println("2. Crear equipo");
            System.out.println("3. Inscribir jugador en equipo");
            System.out.println("4. Ver personas y equipos");
            System.out.println("5. Generar calendario");
            System.out.println("6. Consultar partidos");
            System.out.println("7. Reprogramar partido");
            System.out.println("8. Registrar asistencia o incomparecencia");
            System.out.println("9. Registrar gol en acta");
            System.out.println("10. Registrar tarjeta en acta");
            System.out.println("11. Ver acta del partido");
            System.out.println("12. Finalizar partido y registrar resultado");
            System.out.println("13. Tabla de posiciones");
            System.out.println("14. Salir");
            opcion = pedirEntero("Opcion: ", 1, 14);
            switch (opcion) {
                case 1: registrarPersona(); break;
                case 2: crearEquipo(); break;
                case 3: inscribirJugador(); break;
                case 4: verRegistros(); break;
                case 5: generarCalendario(); break;
                case 6: consultarPartidos(); break;
                case 7: reprogramarPartido(); break;
                case 8: registrarAsistencia(); break;
                case 9: registrarGol(); break;
                case 10: registrarTarjeta(); break;
                case 11: verActa(); break;
                case 12: finalizarPartido(); break;
                case 13: verTabla(); break;
                case 14: System.out.println("Saliendo del sistema..."); break;
            }
        } while (opcion != 14);
        entrada.close();
    }

    static String pedirTexto(String mensaje) {
        String dato = "";
        while (dato.trim().isEmpty()) {
            System.out.print(mensaje);
            dato = entrada.nextLine().trim();
            if (dato.isEmpty()) System.out.println("Error: no puede estar vacio.");
        }
        return dato;
    }

    static int pedirEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            boolean valido = !texto.isEmpty();
            for (int i = 0; i < texto.length(); i++) {
                if (texto.charAt(i) < '0' || texto.charAt(i) > '9') valido = false;
            }
            if (valido) {
                int numero = 0;
                // Limita el tamano antes de convertir para evitar valores demasiado grandes.
                if (texto.length() <= 9) numero = Integer.parseInt(texto);
                else valido = false;
                if (valido && numero >= minimo && numero <= maximo) return numero;
            }
            System.out.println("Error: ingrese un entero entre " + minimo + " y " + maximo + ".");
        }
    }

    static boolean diezDigitos(String dato) {
        if (dato.length() != 10) return false;
        for (int i = 0; i < dato.length(); i++) {
            if (dato.charAt(i) < '0' || dato.charAt(i) > '9') return false;
        }
        return true;
    }

    static boolean soloLetras(String dato) {
        if (dato.isEmpty()) return false;
        for (int i = 0; i < dato.length(); i++) {
            char c = dato.charAt(i);
            if (!Character.isLetter(c) && c != ' ') return false;
        }
        return true;
    }

    static Persona buscarPersona(String cedula) {
        for (Persona p : personas) if (p.cedula.equals(cedula)) return p;
        return null;
    }

    static Jugador buscarJugador(String cedula, Equipo equipo) {
        for (Jugador j : equipo.jugadores) if (j.cedula.equals(cedula)) return j;
        return null;
    }

    static void registrarPersona() {
        String nombre = pedirTexto("Nombre: ");
        while (!soloLetras(nombre)) {
            System.out.println("Solo se permiten letras.");
            nombre = pedirTexto("Nombre: ");
        }
        String apellido = pedirTexto("Apellido: ");
        while (!soloLetras(apellido)) {
            System.out.println("Solo se permiten letras.");
            apellido = pedirTexto("Apellido: ");
        }
        int edad = pedirEntero("Edad (14-60): ", 14, 60);
        String cedula = pedirTexto("Cedula (10 digitos): ");
        while (!diezDigitos(cedula) || buscarPersona(cedula) != null) {
            System.out.println("Cedula invalida o ya registrada.");
            cedula = pedirTexto("Cedula (10 digitos): ");
        }
        String telefono = pedirTexto("Telefono (10 digitos): ");
        boolean valido = false;
        while (!valido) {
            valido = diezDigitos(telefono);
            for (Persona p : personas) if (p.telefono.equals(telefono)) valido = false;
            if (!valido) {
                System.out.println("Telefono invalido o ya registrado.");
                telefono = pedirTexto("Telefono: ");
            }
        }
        personas.add(new Persona(nombre, apellido, edad, cedula, telefono));
        System.out.println("Persona registrada correctamente.");
    }

    static void crearEquipo() {
        String nombre = pedirTexto("Nombre del equipo: ");
        for (Equipo e : equipos) {
            if (e.nombre.equalsIgnoreCase(nombre)) {
                System.out.println("Ese equipo ya existe.");
                return;
            }
        }
        System.out.println("1. Juvenil (14-17)  2. Senior (18-39)  3. Master (40-60)");
        int categoria = pedirEntero("Categoria: ", 1, 3);
        equipos.add(new Equipo(nombre, categoria));
        System.out.println("Equipo creado correctamente.");
    }

    static Equipo seleccionarEquipo() {
        if (equipos.isEmpty()) {
            System.out.println("Primero registre equipos.");
            return null;
        }
        for (int i = 0; i < equipos.size(); i++) {
            Equipo e = equipos.get(i);
            System.out.println((i + 1) + ". " + e.nombre + " - " + e.nombreCategoria());
        }
        int numero = pedirEntero("Seleccione equipo (0 para cancelar): ", 0, equipos.size());
        if (numero == 0) return null;
        return equipos.get(numero - 1);
    }

    static void inscribirJugador() {
        Equipo equipo = seleccionarEquipo();
        if (equipo == null) return;
        String cedula = pedirTexto("Cedula de la persona registrada: ");
        Persona persona = buscarPersona(cedula);
        if (persona == null) {
            System.out.println("La persona no existe. Registrela primero en la opcion 1.");
            return;
        }
        if ((equipo.categoria == 1 && (persona.edad < 14 || persona.edad > 17)) ||
            (equipo.categoria == 2 && (persona.edad < 18 || persona.edad > 39)) ||
            (equipo.categoria == 3 && persona.edad < 40)) {
            System.out.println("La edad no corresponde a la categoria del equipo.");
            return;
        }
        for (Equipo e : equipos) {
            if (e.categoria == equipo.categoria && buscarJugador(cedula, e) != null) {
                System.out.println("Esta persona ya pertenece a un equipo de la misma categoria.");
                return;
            }
        }
        boolean capitan = equipo.capitan == null;
        Jugador jugador = new Jugador(persona, capitan);
        equipo.jugadores.add(jugador);
        if (capitan) equipo.capitan = jugador;
        System.out.println("Jugador inscrito. " + (capitan ? "Es el capitan del equipo." : ""));
    }

    static void verRegistros() {
        System.out.println("\n--- PERSONAS ---");
        if (personas.isEmpty()) System.out.println("Sin personas registradas.");
        for (Persona p : personas) System.out.println(p.nombre + " " + p.apellido + " | " + p.cedula + " | " + p.edad + " anios");
        System.out.println("\n--- EQUIPOS ---");
        if (equipos.isEmpty()) System.out.println("Sin equipos registrados.");
        for (Equipo e : equipos) {
            System.out.println(e.nombre + " (" + e.nombreCategoria() + ")");
            if (e.jugadores.isEmpty()) System.out.println("  Sin jugadores inscritos.");
            for (Jugador j : e.jugadores) {
                System.out.println("  " + j.nombre + " " + j.apellido + " | " + j.cedula +
                    " | " + j.estado + " | Amarillas: " + j.amarillas + (j.capitan ? " | CAPITAN" : ""));
            }
        }
    }

    static void generarCalendario() {
        if (!partidos.isEmpty()) {
            System.out.println("Ya existe un calendario. No se sobrescribira para conservar las actas.");
            return;
        }
        String cancha = pedirTexto("Nombre de la cancha: ");
        String fecha = pedirTexto("Fecha inicial (dd-mm-aaaa): ");
        int hora = pedirEntero("Hora de inicio (0-23): ", 0, 23);
        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                Equipo local = equipos.get(i);
                Equipo visita = equipos.get(j);
                if (local.categoria == visita.categoria && !local.jugadores.isEmpty() && !visita.jugadores.isEmpty()) {
                    // Horarios consecutivos: al llegar a las 24 horas se cambia al dia siguiente manualmente.
                    if (hora > 23) {
                        System.out.println("Se alcanzo el limite horario. Genere los demas encuentros en otra jornada.");
                        consultarPartidos();
                        return;
                    }
                    partidos.add(new Partido(partidos.size() + 1, local, visita, cancha, fecha, hora));
                    hora++;
                }
            }
        }
        if (partidos.isEmpty()) System.out.println("Se requieren dos equipos de la misma categoria con jugadores inscritos.");
        else consultarPartidos();
    }

    static void consultarPartidos() {
        if (partidos.isEmpty()) System.out.println("No hay partidos programados.");
        for (Partido p : partidos) {
            System.out.println(p.id + ". [" + p.estado + "] " + p.local.nombre + " vs " + p.visitante.nombre +
                " | " + p.fecha + " " + p.hora + ":00 | " + p.cancha + " | " + p.local.nombreCategoria());
        }
    }

    static Partido seleccionarPartido() {
        if (partidos.isEmpty()) {
            System.out.println("No hay partidos registrados.");
            return null;
        }
        consultarPartidos();
        int indice = pedirEntero("Numero del partido (0 para cancelar): ", 0, partidos.size());
        if (indice == 0) return null;
        return partidos.get(indice - 1);
    }

    static void reprogramarPartido() {
        Partido p = seleccionarPartido();
        if (p == null) return;
        if (p.finalizado) {
            System.out.println("No se puede reprogramar un partido finalizado.");
            return;
        }
        String fecha = pedirTexto("Nueva fecha (dd-mm-aaaa): ");
        int hora = pedirEntero("Nueva hora (0-23): ", 0, 23);
        for (Partido otro : partidos) {
            if (otro != p && otro.cancha.equalsIgnoreCase(p.cancha) && otro.fecha.equals(fecha) && otro.hora == hora) {
                System.out.println("Error: la cancha ya esta ocupada en esa fecha y hora.");
                return;
            }
        }
        p.fecha = fecha;
        p.hora = hora;
        p.estado = "Reprogramado";
        System.out.println("Partido reprogramado correctamente.");
    }

    static void registrarAsistencia() {
        Partido p = seleccionarPartido();
        if (p == null) return;
        if (p.finalizado) {
            System.out.println("Este partido ya esta finalizado.");
            return;
        }
        System.out.println("1. Ambos presentes");
        System.out.println("2. No se presenta local");
        System.out.println("3. No se presenta visitante");
        System.out.println("4. No se presenta ninguno");
        int opcion = pedirEntero("Opcion: ", 1, 4);
        if (opcion == 1) {
            p.estado = "En juego";
            System.out.println("Partido habilitado para registrar goles y tarjetas.");
        } else {
            if (!p.acta.goles.isEmpty() || !p.acta.tarjetas.isEmpty()) {
                System.out.println("El acta ya tiene eventos. No se puede declarar incomparecencia.");
                return;
            }
            p.finalizado = true;
            if (opcion == 2) { p.golesLocal = 0; p.golesVisitante = 3; p.estado = "No se presento local"; }
            if (opcion == 3) { p.golesLocal = 3; p.golesVisitante = 0; p.estado = "No se presento visitante"; }
            if (opcion == 4) { p.golesLocal = 0; p.golesVisitante = 0; p.estado = "Ninguno se presento"; }
            // Los marcadores 3-0 se usan como regla de ejemplo para incomparecencia.
            // Cuando no se presenta ninguno, no se suman puntos ni partidos jugados.
            if (opcion != 4) actualizarPosiciones(p);
            System.out.println("Incomparecencia registrada. Resultado: " + p.golesLocal + "-" + p.golesVisitante);
        }
    }

    static Jugador seleccionarJugador(Partido p) {
        System.out.println("1. " + p.local.nombre + "   2. " + p.visitante.nombre);
        int opcion = pedirEntero("Equipo del jugador: ", 1, 2);
        Equipo equipo = opcion == 1 ? p.local : p.visitante;
        String cedula = pedirTexto("Cedula del jugador: ");
        Jugador jugador = buscarJugador(cedula, equipo);
        if (jugador == null) System.out.println("Jugador no inscrito en ese equipo.");
        return jugador;
    }

    static void registrarGol() {
        Partido p = seleccionarPartido();
        if (p == null) return;
        if (p.finalizado || !p.estado.equals("En juego")) {
            System.out.println("Primero registre la asistencia de ambos equipos. El partido no debe estar finalizado.");
            return;
        }
        Jugador jugador = seleccionarJugador(p);
        if (jugador == null) return;
        if (jugador.estado.equals("Suspendido")) {
            System.out.println("El jugador esta suspendido.");
            return;
        }
        int minuto = pedirEntero("Minuto del gol (0-120): ", 0, 120);
        p.acta.goles.add(new Gol(p.acta.goles.size() + 1, minuto, jugador));
        System.out.println("Gol registrado correctamente.");
    }

    static void registrarTarjeta() {
        Partido p = seleccionarPartido();
        if (p == null) return;
        if (p.finalizado || !p.estado.equals("En juego")) {
            System.out.println("Primero registre la asistencia. El partido no debe estar finalizado.");
            return;
        }
        Jugador jugador = seleccionarJugador(p);
        if (jugador == null) return;
        if (jugador.estado.equals("Suspendido")) {
            System.out.println("El jugador ya esta suspendido.");
            return;
        }
        System.out.println("1. Amarilla   2. Roja");
        int tipo = pedirEntero("Tipo de tarjeta: ", 1, 2);
        int minuto = pedirEntero("Minuto (0-120): ", 0, 120);
        p.acta.tarjetas.add(new Tarjeta(p.acta.tarjetas.size() + 1, tipo == 1 ? "Amarilla" : "Roja", minuto, jugador));
        if (tipo == 1) {
            jugador.amarillas++;
            if (jugador.amarillas >= 3) jugador.estado = "Suspendido";
        } else {
            jugador.estado = "Suspendido";
        }
        System.out.println("Tarjeta registrada. Amarillas acumuladas: " + jugador.amarillas + ". Estado: " + jugador.estado);
    }

    static void verActa() {
        Partido p = seleccionarPartido();
        if (p == null) return;
        System.out.println("--- ACTA PARTIDO " + p.id + " ---");
        System.out.println(p.local.nombre + " vs " + p.visitante.nombre + " | " + p.estado);
        System.out.println("GOLES:");
        if (p.acta.goles.isEmpty()) System.out.println("Sin goles.");
        for (Gol g : p.acta.goles) System.out.println(g.minuto + "' " + g.jugador.nombre + " " + g.jugador.apellido);
        System.out.println("TARJETAS:");
        if (p.acta.tarjetas.isEmpty()) System.out.println("Sin tarjetas.");
        for (Tarjeta t : p.acta.tarjetas) System.out.println(t.minuto + "' " + t.tipo + " - " + t.jugador.nombre + " " + t.jugador.apellido);
        if (p.finalizado) System.out.println("Resultado: " + p.golesLocal + "-" + p.golesVisitante);
    }

    static void finalizarPartido() {
        Partido p = seleccionarPartido();
        if (p == null) return;
        if (p.finalizado || !p.estado.equals("En juego")) {
            System.out.println("El partido no esta en juego o ya fue finalizado.");
            return;
        }
        int local = 0;
        int visitante = 0;
        for (Gol g : p.acta.goles) {
            if (buscarJugador(g.jugador.cedula, p.local) != null) local++;
            else visitante++;
        }
        System.out.println("Marcador segun goles registrados en acta: " + local + " - " + visitante);
        System.out.println("1. Confirmar resultado   2. Cancelar");
        int opcion = pedirEntero("Opcion: ", 1, 2);
        if (opcion == 2) return;
        p.golesLocal = local;
        p.golesVisitante = visitante;
        p.finalizado = true;
        p.estado = "Finalizado";
        actualizarPosiciones(p);
        System.out.println("Resultado finalizado y tabla actualizada.");
    }

    static void actualizarPosiciones(Partido p) {
        p.local.jugados++;
        p.visitante.jugados++;
        p.local.golesFavor += p.golesLocal;
        p.local.golesContra += p.golesVisitante;
        p.visitante.golesFavor += p.golesVisitante;
        p.visitante.golesContra += p.golesLocal;
        if (p.golesLocal > p.golesVisitante) {
            p.local.ganados++; p.visitante.perdidos++; p.local.puntos += 3;
        } else if (p.golesLocal < p.golesVisitante) {
            p.visitante.ganados++; p.local.perdidos++; p.visitante.puntos += 3;
        } else {
            p.local.empatados++; p.visitante.empatados++;
            p.local.puntos++; p.visitante.puntos++;
        }
    }

    static void verTabla() {
        System.out.println("1. Juvenil  2. Senior  3. Master");
        int categoria = pedirEntero("Categoria: ", 1, 3);
        ArrayList<Equipo> tabla = new ArrayList<Equipo>();
        for (Equipo e : equipos) if (e.categoria == categoria) tabla.add(e);
        for (int i = 0; i < tabla.size(); i++) {
            for (int j = i + 1; j < tabla.size(); j++) {
                Equipo a = tabla.get(i), b = tabla.get(j);
                int diferenciaA = a.golesFavor - a.golesContra;
                int diferenciaB = b.golesFavor - b.golesContra;
                if (b.puntos > a.puntos || (b.puntos == a.puntos && diferenciaB > diferenciaA)) {
                    tabla.set(i, b);
                    tabla.set(j, a);
                }
            }
        }
        System.out.println("EQUIPO | PJ | G | E | P | GF | GC | DG | PTS");
        if (tabla.isEmpty()) System.out.println("Sin equipos en esta categoria.");
        for (Equipo e : tabla) {
            System.out.println(e.nombre + " | " + e.jugados + " | " + e.ganados + " | " + e.empatados + " | " +
                e.perdidos + " | " + e.golesFavor + " | " + e.golesContra + " | " +
                (e.golesFavor - e.golesContra) + " | " + e.puntos);
        }
    }
}
