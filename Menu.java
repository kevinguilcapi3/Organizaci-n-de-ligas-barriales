import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Menu extends JFrame implements ActionListener {
    private SistemaModulo4 sistema;
    private JTextArea salida;
    private JButton botonTabla;
    private JButton botonConsulta;
    private JButton botonRegistro;

    public Menu() {
        sistema = new SistemaModulo4();
        setTitle("Liga Barrial - Módulo 4");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("MÓDULO 4 - RESULTADOS Y CONSULTAS", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(3, 1, 10, 10));
        botones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        botonTabla = new JButton("RF8 - Tabla de posiciones");
        botonConsulta = new JButton("RF10 - Calendario y resultados");
        botonRegistro = new JButton("Registrar datos y resultados");
        botones.add(botonTabla);
        botones.add(botonConsulta);
        botones.add(botonRegistro);
        add(botones, BorderLayout.WEST);
        botonTabla.addActionListener(this);
        botonConsulta.addActionListener(this);
        botonRegistro.addActionListener(this);

        salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(salida), BorderLayout.CENTER);
        salida.setText("MÓDULO 4 - LIGA BARRIAL\n\n"
                + "1. RF8 - Tabla de posiciones\n"
                + "2. RF10 - Calendario y resultados\n"
                + "3. Registrar datos y resultados\n\n"
                + "Categorías: Juvenil, Abierta y Máster.\n"
                + "Sin equipos ni partidos precargados.");
    }

    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == botonTabla) verTabla();
        else if (evento.getSource() == botonConsulta) verConsultas();
        else if (evento.getSource() == botonRegistro) menuRegistro();
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void correcto(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private String elegirCategoria() {
        String[] opciones = {
            "1. Juvenil (Sub-18) - Menores de 18 años",
            "2. Abierta / Sénior - De 18 a 39 años",
            "3. Máster (+40) - 40 años en adelante"
        };
        Object valor = JOptionPane.showInputDialog(this, "Seleccione la categoría:",
                "Categorías", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (valor == null) return null;
        if (valor.equals(opciones[0])) return SistemaModulo4.JUVENIL;
        if (valor.equals(opciones[1])) return SistemaModulo4.ABIERTA;
        return SistemaModulo4.MASTER;
    }

    private String pedirTexto(String pregunta) {
        while (true) {
            String dato = JOptionPane.showInputDialog(this, pregunta);
            if (dato == null) return null;
            dato = dato.trim();
            if (!dato.isEmpty() && dato.length() <= 30) return dato;
            error("Escriba un nombre de 1 a 30 caracteres.");
        }
    }

    // Se revisan los caracteres antes de convertir el número.
    private Integer pedirGoles(String pregunta) {
        while (true) {
            String dato = JOptionPane.showInputDialog(this,
                    pregunta + "\nIngrese un número entero de 0 a 30:");
            if (dato == null) return null;
            dato = dato.trim();
            boolean valido = dato.length() >= 1 && dato.length() <= 2;
            for (int i = 0; i < dato.length(); i++) {
                if (dato.charAt(i) < '0' || dato.charAt(i) > '9') valido = false;
            }
            if (valido) {
                int numero = Integer.parseInt(dato);
                if (numero >= 0 && numero <= 30) return numero;
            }
            error("Dato incorrecto. Escriba solamente números del 0 al 30.");
        }
    }

    private String pedirFecha(boolean obligatoria) {
        while (true) {
            String dato = JOptionPane.showInputDialog(this,
                    "Fecha AAAA-MM-DD (ejemplo: 2027-01-20).\n"
                    + "Si es opcional, puede dejarla vacía:");
            if (dato == null) return null;
            dato = dato.trim();
            if (dato.isEmpty() && !obligatoria) return "";
            if (sistema.fechaValida(dato)) return dato;
            error("Fecha incorrecta. Inténtelo otra vez.");
        }
    }

    private String elegirEquipo(String categoria) {
        ArrayList<String> opciones = new ArrayList<String>();
        opciones.add("Todos");
        for (Posicion p : sistema.getEquipos()) {
            if (p.getCategoria().equals(categoria)) opciones.add(p.getEquipo());
        }
        Object valor = JOptionPane.showInputDialog(this, "Seleccione un equipo:",
                "Equipos", JOptionPane.QUESTION_MESSAGE,
                null, opciones.toArray(), opciones.get(0));
        if (valor == null) return null;
        return valor.toString();
    }

    private String crearTabla(String categoria) {
        ArrayList<Posicion> lista = sistema.tabla(categoria);
        String texto = "CATEGORÍA: " + categoria + "\n\n";
        if (lista.isEmpty()) return texto + "Todavía no hay equipos registrados.\n";
        texto += "EQUIPO | PJ | PG | PE | PP | GF | GC | DG | PUNTOS\n";
        int lugar = 1;
        for (Posicion p : lista) {
            texto += lugar + ". " + p.getEquipo()
                    + " | " + p.getJugados() + " | " + p.getGanados()
                    + " | " + p.getEmpatados() + " | " + p.getPerdidos()
                    + " | " + p.getGolesFavor() + " | " + p.getGolesContra()
                    + " | " + p.getDiferencia() + " | " + p.getPuntos() + "\n";
            lugar++;
        }
        return texto;
    }

    private void verTabla() {
        String categoria = elegirCategoria();
        if (categoria == null) return;
        salida.setText("TABLA DE POSICIONES (RF8)\n\n" + crearTabla(categoria));
    }

    private void verConsultas() {
        String categoria = elegirCategoria();
        if (categoria == null) return;
        String equipo = elegirEquipo(categoria);
        if (equipo == null) return;
        String fecha = pedirFecha(false);
        if (fecha == null) return;
        ArrayList<PartidoConsulta> lista = sistema.consultar(categoria, equipo, fecha);
        String texto = "CALENDARIO Y RESULTADOS (RF10)\n\n";
        if (lista.isEmpty()) texto += "No hay partidos para esos filtros.\n";
        for (PartidoConsulta p : lista) texto += p + "\n";
        salida.setText(texto + "\n" + crearTabla(categoria));
    }

    private void menuRegistro() {
        String[] opciones = {"Agregar equipo", "Programar partido", "Registrar resultado"};
        Object valor = JOptionPane.showInputDialog(this, "Seleccione la opción:",
                "Registro", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (valor == null) return;
        if (valor.equals(opciones[0])) agregarEquipo();
        else if (valor.equals(opciones[1])) agregarPartido();
        else registrarResultado();
    }

    private void agregarEquipo() {
        String categoria = elegirCategoria();
        if (categoria == null) return;
        while (true) {
            String nombre = pedirTexto("Nombre del equipo:");
            if (nombre == null) return;
            if (sistema.agregarEquipo(nombre, categoria)) {
                correcto("Equipo registrado correctamente.");
                salida.setText(crearTabla(categoria));
                return;
            }
            error(sistema.getUltimoError());
        }
    }

    private void agregarPartido() {
        String categoria = elegirCategoria();
        if (categoria == null) return;
        ArrayList<String> equipos = new ArrayList<String>();
        for (Posicion p : sistema.getEquipos()) {
            if (p.getCategoria().equals(categoria)) equipos.add(p.getEquipo());
        }
        if (equipos.size() < 2) {
            error("Primero registre dos equipos de esta categoría.");
            return;
        }
        while (true) {
            Object local = JOptionPane.showInputDialog(this, "Equipo local:",
                    "Partido", JOptionPane.QUESTION_MESSAGE,
                    null, equipos.toArray(), equipos.get(0));
            if (local == null) return;
            Object visitante = JOptionPane.showInputDialog(this, "Equipo visitante:",
                    "Partido", JOptionPane.QUESTION_MESSAGE,
                    null, equipos.toArray(), equipos.get(0));
            if (visitante == null) return;
            if (local.equals(visitante)) {
                error("Un equipo no puede jugar contra sí mismo.");
                continue;
            }
            String fecha = pedirFecha(true);
            if (fecha == null) return;
            PartidoConsulta partido = sistema.agregarPartido(categoria,
                    local.toString(), visitante.toString(), fecha);
            if (partido != null) {
                correcto("Partido programado correctamente.");
                salida.setText(partido.toString());
                return;
            }
            error(sistema.getUltimoError());
        }
    }

    private void registrarResultado() {
        ArrayList<PartidoConsulta> pendientes = new ArrayList<PartidoConsulta>();
        for (PartidoConsulta p : sistema.getPartidos()) {
            if (!p.estaFinalizado()) pendientes.add(p);
        }
        if (pendientes.isEmpty()) {
            error("No hay partidos pendientes.");
            return;
        }
        Object valor = JOptionPane.showInputDialog(this, "Seleccione el partido:",
                "Resultados", JOptionPane.QUESTION_MESSAGE,
                null, pendientes.toArray(), pendientes.get(0));
        if (valor == null) return;
        PartidoConsulta partido = (PartidoConsulta) valor;
        Integer local = pedirGoles("Goles de " + partido.getLocal());
        if (local == null) return;
        Integer visitante = pedirGoles("Goles de " + partido.getVisitante());
        if (visitante == null) return;
        if (sistema.registrarResultado(partido, local, visitante)) {
            correcto("Resultado registrado. Tabla actualizada.");
            salida.setText(partido + "\n\n" + crearTabla(partido.getCategoria()));
        } else {
            error(sistema.getUltimoError());
        }
    }
}
