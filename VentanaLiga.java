import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaLiga extends JFrame {
    private final Color morado = new Color(91, 43, 116);
    private final Color violeta = new Color(141, 91, 162);
    private final Color fondo = new Color(252, 247, 255);
    private final Color rosa = new Color(250, 218, 242);
    private final Font texto = new Font("SansSerif", Font.PLAIN, 15);
    private JPanel contenido, menu;
    private String pagina = "Inicio";

    public VentanaLiga() {
        setTitle("Liga Barrial - Campeonato");
        setSize(1080, 730);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(fondo);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(violeta);
        superior.setBorder(new EmptyBorder(13, 25, 13, 20));
        JLabel logo = new JLabel("⚽  Liga Barrial");
        logo.setFont(new Font("SansSerif", Font.BOLD, 27));
        logo.setForeground(Color.WHITE);
        superior.add(logo, BorderLayout.WEST);
        add(superior, BorderLayout.NORTH);

        menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(238, 224, 247));
        menu.setPreferredSize(new Dimension(225, 600));
        menu.setBorder(new EmptyBorder(26, 12, 12, 12));
        add(menu, BorderLayout.WEST);

        contenido = new JPanel(new BorderLayout());
        contenido.setBackground(fondo);
        contenido.setBorder(new EmptyBorder(28, 28, 28, 28));
        add(contenido, BorderLayout.CENTER);
        navegar("Inicio");
    }

    private JButton boton(String nombre, Runnable accion) {
        JButton b = new JButton(nombre);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(violeta);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(11, 18, 11, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> accion.run());
        return b;
    }

    private void navegar(String nombre) {
        pagina = nombre;
        menu.removeAll();
        String[] paginas = {"Inicio", "Equipos", "Jugadores", "Partidos", "Actas", "Posiciones"};
        String[] iconos = {"⌂  ", "♟  ", "♙  ", "▦  ", "▤  ", "★  "};
        for (int i = 0; i < paginas.length; i++) {
            final String destino = paginas[i];
            JButton b = boton(iconos[i] + destino, () -> navegar(destino));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setMaximumSize(new Dimension(205, 52));
            b.setBackground(nombre.equals(destino) ? morado : new Color(238, 224, 247));
            b.setForeground(nombre.equals(destino) ? Color.WHITE : morado);
            menu.add(b);
            menu.add(Box.createVerticalStrut(9));
        }
        contenido.removeAll();
        switch (nombre) {
            case "Equipos": vistaEquipos(); break;
            case "Jugadores": vistaJugadores(); break;
            case "Partidos": vistaPartidos(); break;
            case "Actas": vistaActas(); break;
            case "Posiciones": vistaPosiciones(); break;
            default: vistaInicio();
        }
        menu.revalidate(); menu.repaint();
        contenido.revalidate(); contenido.repaint();
    }

    private JLabel titulo(String s) {
        JLabel label = new JLabel(s);
        label.setFont(new Font("SansSerif", Font.BOLD, 32));
        label.setForeground(morado);
        label.setBorder(new EmptyBorder(0, 0, 20, 0));
        return label;
    }
    private JPanel panelBotones(JButton... botones) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 4));
        p.setBackground(fondo);
        for (JButton b : botones) p.add(b);
        return p;
    }
    private JPanel vertical() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(fondo);
        return p;
    }
    private JTable tabla(String[] cols, Object[][] filas) {
        DefaultTableModel model = new DefaultTableModel(filas, cols) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable t = new JTable(model);
        t.setFont(texto);
        t.setRowHeight(34);
        t.setBackground(Color.WHITE);
        t.setForeground(morado);
        t.setSelectionBackground(rosa);
        t.setGridColor(new Color(223, 199, 234));
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        t.getTableHeader().setBackground(rosa);
        t.getTableHeader().setForeground(morado);
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));
        return t;
    }
    private JScrollPane desplazamiento(JTable t) {
        JScrollPane sc = new JScrollPane(t);
        sc.setBorder(BorderFactory.createLineBorder(new Color(210, 175, 225)));
        return sc;
    }
    private void mensaje(String s) { JOptionPane.showMessageDialog(this, s, "Liga Barrial", JOptionPane.INFORMATION_MESSAGE); }
    private String pedir(String etiqueta) {
        String s = JOptionPane.showInputDialog(this, etiqueta);
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) { mensaje("El campo no puede quedar vacío."); return null; }
        return s;
    }
    private Integer entero(String etiqueta, int min, int max) {
        String dato = pedir(etiqueta);
        if (dato == null) return null;
        for (int i = 0; i < dato.length(); i++) if (!Character.isDigit(dato.charAt(i))) { mensaje("Ingrese solo números enteros."); return null; }
        if (dato.length() > 8) { mensaje("Número demasiado grande."); return null; }
        int n = Integer.parseInt(dato);
        if (n < min || n > max) { mensaje("Ingrese un número de " + min + " a " + max + "."); return null; }
        return n;
    }
    private int opcion(String mensaje, String[] opciones) {
        return JOptionPane.showOptionDialog(this, mensaje, "Liga Barrial", JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
    }
    private Equipo elegirEquipo() {
        if (Main.equipos.isEmpty()) { mensaje("Primero cree un equipo."); return null; }
        String[] nombres = new String[Main.equipos.size()];
        for (int i = 0; i < nombres.length; i++) nombres[i] = Main.equipos.get(i).nombre + " (" + Main.equipos.get(i).nombreCategoria() + ")";
        int i = opcion("Seleccione un equipo:", nombres);
        return i < 0 ? null : Main.equipos.get(i);
    }
    private Partido elegirPartido() {
        if (Main.partidos.isEmpty()) { mensaje("No hay partidos. Genere el calendario primero."); return null; }
        String[] nombres = new String[Main.partidos.size()];
        for (int i = 0; i < nombres.length; i++) {
            Partido p = Main.partidos.get(i);
            nombres[i] = "#" + p.id + " " + p.local.nombre + " vs " + p.visitante.nombre;
        }
        int i = opcion("Seleccione un partido:", nombres);
        return i < 0 ? null : Main.partidos.get(i);
    }
    private Jugador elegirJugador(Partido p) {
        int indice = opcion("¿A qué equipo pertenece?", new String[]{p.local.nombre, p.visitante.nombre});
        if (indice < 0) return null;
        Equipo e = indice == 0 ? p.local : p.visitante;
        if (e.jugadores.isEmpty()) { mensaje("Este equipo no tiene jugadores inscritos."); return null; }
        String[] opciones = new String[e.jugadores.size()];
        for (int i = 0; i < opciones.length; i++) opciones[i] = e.jugadores.get(i).nombre + " " + e.jugadores.get(i).apellido + " (" + e.jugadores.get(i).cedula + ")";
        int n = opcion("Seleccione un jugador:", opciones);
        return n < 0 ? null : e.jugadores.get(n);
    }

    private JPanel tarjeta(String nombre, int cantidad, Color color) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(color);
        p.setBorder(new CompoundBorder(BorderFactory.createLineBorder(new Color(234, 214, 243)), new EmptyBorder(12, 14, 12, 14)));
        JLabel a = new JLabel(nombre, SwingConstants.CENTER);
        a.setForeground(morado); a.setFont(new Font("SansSerif", Font.BOLD, 23));
        JLabel b = new JLabel("" + cantidad, SwingConstants.CENTER);
        b.setForeground(morado); b.setFont(new Font("SansSerif", Font.BOLD, 43));
        p.add(a); p.add(b); return p;
    }
    private Object[][] filasPartidos(boolean pendientes) {
        ArrayList<Object[]> filas = new ArrayList<Object[]>();
        for (Partido p : Main.partidos) {
            if (pendientes && p.finalizado) continue;
            filas.add(new Object[]{p.fecha + " " + p.hora + ":00", p.local.nombre, "vs", p.visitante.nombre, p.estado});
        }
        return filas.toArray(new Object[0][]);
    }
    private void vistaInicio() {
        contenido.add(titulo("⚽ Panel Principal"), BorderLayout.NORTH);
        JPanel principal = vertical();
        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 18, 0)); tarjetas.setBackground(fondo);
        int cantidadJugadores = 0;
        for (Equipo e : Main.equipos) cantidadJugadores += e.jugadores.size();
        tarjetas.add(tarjeta("♟ Equipos", Main.equipos.size(), new Color(231, 210, 244)));
        tarjetas.add(tarjeta("♙ Jugadores", cantidadJugadores, new Color(250, 217, 242)));
        tarjetas.add(tarjeta("▦ Partidos", Main.partidos.size(), new Color(231, 210, 244)));
        tarjetas.setPreferredSize(new Dimension(0, 165));
        principal.add(tarjetas, BorderLayout.NORTH);
        JPanel abajo = vertical();
        JLabel sub = new JLabel("Próximos Partidos");
        sub.setOpaque(true); sub.setBackground(violeta); sub.setForeground(Color.WHITE);
        sub.setFont(new Font("SansSerif", Font.BOLD, 23)); sub.setBorder(new EmptyBorder(12, 15, 12, 15));
        abajo.add(sub, BorderLayout.NORTH);
        abajo.add(desplazamiento(tabla(new String[]{"Fecha", "Local", "", "Visitante", "Estado"}, filasPartidos(true))), BorderLayout.CENTER);
        principal.add(abajo, BorderLayout.CENTER);
        contenido.add(principal, BorderLayout.CENTER);
    }
    private void vistaEquipos() {
        contenido.add(titulo("♟ Equipos"), BorderLayout.NORTH);
        JPanel p = vertical();
        p.add(panelBotones(boton("+ Crear equipo", () -> crearEquipo()), boton("Ver jugadores", () -> {Equipo e = elegirEquipo(); if (e != null) mostrarPlantilla(e);})), BorderLayout.NORTH);
        Object[][] datos = new Object[Main.equipos.size()][4];
        for (int i = 0; i < datos.length; i++) {
            Equipo e = Main.equipos.get(i);
            datos[i] = new Object[]{e.nombre, e.nombreCategoria(), e.jugadores.size(), e.capitan == null ? "Sin capitán" : e.capitan.nombre + " " + e.capitan.apellido};
        }
        p.add(desplazamiento(tabla(new String[]{"Equipo", "Categoría", "Jugadores", "Capitán"}, datos)), BorderLayout.CENTER);
        contenido.add(p, BorderLayout.CENTER);
    }
    private void crearEquipo() {
        String nombre = pedir("Nombre del equipo:"); if (nombre == null) return;
        for (Equipo e : Main.equipos) if (e.nombre.equalsIgnoreCase(nombre)) { mensaje("Ese equipo ya existe."); return; }
        int cat = opcion("Categoría:", new String[]{"Juvenil (14-17)", "Senior (18-39)", "Máster (40-60)"});
        if (cat < 0) return;
        Main.equipos.add(new Equipo(nombre, cat + 1)); mensaje("Equipo registrado correctamente."); navegar("Equipos");
    }
    private void vistaJugadores() {
        contenido.add(titulo("♙ Jugadores"), BorderLayout.NORTH);
        JPanel p = vertical();
        p.add(panelBotones(boton("+ Registrar persona", () -> registrarPersona()), boton("+ Inscribir jugador", () -> inscribirJugador())), BorderLayout.NORTH);
        ArrayList<Object[]> datos = new ArrayList<Object[]>();
        for (Equipo e : Main.equipos) for (Jugador j : e.jugadores) datos.add(new Object[]{j.cedula, j.nombre + " " + j.apellido, j.edad, e.nombre, j.estado, j.amarillas});
        p.add(desplazamiento(tabla(new String[]{"Cédula", "Jugador", "Edad", "Equipo", "Estado", "Amarillas"}, datos.toArray(new Object[0][]))), BorderLayout.CENTER);
        contenido.add(p, BorderLayout.CENTER);
    }
    private void registrarPersona() {
        String nom = pedir("Nombre:"); if (nom == null) return;
        String ape = pedir("Apellido:"); if (ape == null) return;
        if (!Main.soloLetras(nom) || !Main.soloLetras(ape)) { mensaje("Nombre y apellido solo pueden tener letras."); return; }
        Integer edad = entero("Edad (14-60):", 14, 60); if (edad == null) return;
        String ced = pedir("Cédula (10 dígitos):"); if (ced == null) return;
        if (!Main.diezDigitos(ced) || Main.buscarPersona(ced) != null) { mensaje("Cédula inválida o duplicada."); return; }
        String tel = pedir("Teléfono (10 dígitos):"); if (tel == null) return;
        if (!Main.diezDigitos(tel)) { mensaje("El teléfono debe tener 10 dígitos."); return; }
        for (Persona persona : Main.personas) if (persona.telefono.equals(tel)) { mensaje("El teléfono ya está registrado."); return; }
        Main.personas.add(new Persona(nom, ape, edad, ced, tel)); mensaje("Persona registrada. Ya puede inscribirla en un equipo."); navegar("Jugadores");
    }
    private void inscribirJugador() {
        Equipo e = elegirEquipo(); if (e == null) return;
        if (Main.personas.isEmpty()) { mensaje("Registre una persona primero."); return; }
        String ced = pedir("Cédula de la persona registrada:"); if (ced == null) return;
        Persona persona = Main.buscarPersona(ced);
        if (persona == null) { mensaje("No existe una persona con esa cédula."); return; }
        if ((e.categoria == 1 && (persona.edad < 14 || persona.edad > 17)) ||
            (e.categoria == 2 && (persona.edad < 18 || persona.edad > 39)) ||
            (e.categoria == 3 && persona.edad < 40)) { mensaje("La edad no corresponde a esta categoría."); return; }
        for (Equipo otro : Main.equipos) if (otro.categoria == e.categoria && Main.buscarJugador(ced, otro) != null) { mensaje("Ya está inscrito en esa categoría."); return; }
        boolean capitan = e.capitan == null;
        Jugador j = new Jugador(persona, capitan); e.jugadores.add(j);
        if (capitan) e.capitan = j;
        mensaje("Jugador inscrito" + (capitan ? " como capitán." : ".")); navegar("Jugadores");
    }
    private void mostrarPlantilla(Equipo e) {
        StringBuilder s = new StringBuilder(e.nombre + " - " + e.nombreCategoria() + "\n\n");
        if (e.jugadores.isEmpty()) s.append("Sin jugadores inscritos.");
        for (Jugador j : e.jugadores) s.append(j.nombre).append(" ").append(j.apellido).append(" - ").append(j.cedula).append(j.capitan ? " [CAPITÁN]" : "").append("\n");
        mensaje(s.toString());
    }
    private void vistaPartidos() {
        contenido.add(titulo("▦ Partidos"), BorderLayout.NORTH);
        JPanel p = vertical();
        p.add(panelBotones(boton("+ Generar calendario", () -> generarCalendario()), boton("Reprogramar", () -> reprogramar()), boton("Registrar asistencia", () -> asistencia()), boton("Finalizar partido", () -> finalizar())), BorderLayout.NORTH);
        p.add(desplazamiento(tabla(new String[]{"Fecha", "Local", "", "Visitante", "Estado"}, filasPartidos(false))), BorderLayout.CENTER);
        contenido.add(p, BorderLayout.CENTER);
    }
    private void generarCalendario() {
        if (!Main.partidos.isEmpty()) { mensaje("El calendario ya existe y no se sobrescribirá."); return; }
        String cancha = pedir("Nombre de la cancha:"); if (cancha == null) return;
        String fecha = pedir("Fecha de inicio (dd-mm-aaaa):"); if (fecha == null) return;
        Integer hora = entero("Hora inicial (0-23):", 0, 23); if (hora == null) return;
        for (int i = 0; i < Main.equipos.size(); i++) for (int j = i + 1; j < Main.equipos.size(); j++) {
            Equipo a = Main.equipos.get(i), b = Main.equipos.get(j);
            if (a.categoria == b.categoria && !a.jugadores.isEmpty() && !b.jugadores.isEmpty() && hora <= 23) {
                Main.partidos.add(new Partido(Main.partidos.size() + 1, a, b, cancha, fecha, hora)); hora++;
            }
        }
        mensaje(Main.partidos.isEmpty() ? "Se necesitan dos equipos de la misma categoría con jugadores." : "Calendario generado correctamente."); navegar("Partidos");
    }
    private void reprogramar() {
        Partido p = elegirPartido(); if (p == null) return;
        if (p.finalizado) { mensaje("No se puede reprogramar un partido finalizado."); return; }
        String fecha = pedir("Nueva fecha (dd-mm-aaaa):"); if (fecha == null) return;
        Integer hora = entero("Nueva hora (0-23):", 0, 23); if (hora == null) return;
        for (Partido otro : Main.partidos) if (otro != p && otro.cancha.equalsIgnoreCase(p.cancha) && otro.fecha.equals(fecha) && otro.hora == hora) { mensaje("La cancha ya está ocupada a esa hora."); return; }
        p.fecha = fecha; p.hora = hora; p.estado = "Reprogramado"; mensaje("Partido reprogramado."); navegar("Partidos");
    }
    private void asistencia() {
        Partido p = elegirPartido(); if (p == null) return;
        if (p.finalizado) { mensaje("Partido finalizado."); return; }
        int op = opcion("Asistencia al partido:", new String[]{"Ambos presentes", "Falta local", "Falta visitante", "No se presenta ninguno"});
        if (op < 0) return;
        if (op == 0) { p.estado = "En juego"; mensaje("Partido habilitado para registrar goles y tarjetas."); }
        else {
            if (!p.acta.goles.isEmpty() || !p.acta.tarjetas.isEmpty()) { mensaje("No se puede declarar incomparecencia con eventos en acta."); return; }
            p.finalizado = true;
            if (op == 1) { p.golesLocal = 0; p.golesVisitante = 3; p.estado = "No se presentó local"; }
            if (op == 2) { p.golesLocal = 3; p.golesVisitante = 0; p.estado = "No se presentó visitante"; }
            if (op == 3) { p.golesLocal = 0; p.golesVisitante = 0; p.estado = "Ninguno se presentó"; }
            if (op != 3) Main.actualizarPosiciones(p);
            mensaje("Incomparecencia registrada.");
        }
        navegar("Partidos");
    }
    private void finalizar() {
        Partido p = elegirPartido(); if (p == null) return;
        if (p.finalizado || !p.estado.equals("En juego")) { mensaje("El partido debe estar en juego y sin finalizar."); return; }
        int local = 0, visita = 0;
        for (Gol g : p.acta.goles) { if (Main.buscarJugador(g.jugador.cedula, p.local) != null) local++; else visita++; }
        int confirma = JOptionPane.showConfirmDialog(this, "¿Confirmar resultado " + local + " - " + visita + "?", "Finalizar", JOptionPane.YES_NO_OPTION);
        if (confirma != JOptionPane.YES_OPTION) return;
        p.golesLocal = local; p.golesVisitante = visita; p.finalizado = true; p.estado = "Finalizado";
        Main.actualizarPosiciones(p); mensaje("Resultado y posiciones actualizados."); navegar("Partidos");
    }
    private void vistaActas() {
        contenido.add(titulo("▤ Actas y disciplina"), BorderLayout.NORTH);
        JPanel p = vertical();
        p.add(panelBotones(boton("+ Registrar gol", () -> gol()), boton("+ Registrar tarjeta", () -> tarjeta()), boton("Ver acta", () -> verActa())), BorderLayout.NORTH);
        ArrayList<Object[]> eventos = new ArrayList<Object[]>();
        for (Partido partido : Main.partidos) {
            for (Gol g : partido.acta.goles) eventos.add(new Object[]{partido.id, "Gol", g.minuto + "'", g.jugador.nombre + " " + g.jugador.apellido});
            for (Tarjeta t : partido.acta.tarjetas) eventos.add(new Object[]{partido.id, "Tarjeta " + t.tipo, t.minuto + "'", t.jugador.nombre + " " + t.jugador.apellido});
        }
        p.add(desplazamiento(tabla(new String[]{"Partido", "Evento", "Minuto", "Jugador"}, eventos.toArray(new Object[0][]))), BorderLayout.CENTER);
        contenido.add(p, BorderLayout.CENTER);
    }
    private void gol() {
        Partido p = elegirPartido(); if (p == null) return;
        if (p.finalizado || !p.estado.equals("En juego")) { mensaje("Registre primero la asistencia de ambos equipos."); return; }
        Jugador j = elegirJugador(p); if (j == null) return;
        if (j.estado.equals("Suspendido")) { mensaje("El jugador está suspendido."); return; }
        Integer minuto = entero("Minuto del gol (0-120):", 0, 120); if (minuto == null) return;
        p.acta.goles.add(new Gol(p.acta.goles.size() + 1, minuto, j)); mensaje("Gol registrado correctamente."); navegar("Actas");
    }
    private void tarjeta() {
        Partido p = elegirPartido(); if (p == null) return;
        if (p.finalizado || !p.estado.equals("En juego")) { mensaje("Registre primero la asistencia de ambos equipos."); return; }
        Jugador j = elegirJugador(p); if (j == null) return;
        if (j.estado.equals("Suspendido")) { mensaje("Jugador suspendido."); return; }
        int tipo = opcion("Tipo de tarjeta:", new String[]{"Amarilla", "Roja"}); if (tipo < 0) return;
        Integer minuto = entero("Minuto de la tarjeta (0-120):", 0, 120); if (minuto == null) return;
        p.acta.tarjetas.add(new Tarjeta(p.acta.tarjetas.size() + 1, tipo == 0 ? "Amarilla" : "Roja", minuto, j));
        if (tipo == 0) { j.amarillas++; if (j.amarillas >= 3) j.estado = "Suspendido"; }
        else j.estado = "Suspendido";
        mensaje("Tarjeta registrada. Estado: " + j.estado); navegar("Actas");
    }
    private void verActa() {
        Partido p = elegirPartido(); if (p == null) return;
        StringBuilder s = new StringBuilder("ACTA #" + p.id + "\n" + p.local.nombre + " vs " + p.visitante.nombre + "\nEstado: " + p.estado + "\n\nGOLES:\n");
        if (p.acta.goles.isEmpty()) s.append("Sin goles\n");
        for (Gol g : p.acta.goles) s.append(g.minuto).append("' ").append(g.jugador.nombre).append(" ").append(g.jugador.apellido).append("\n");
        s.append("\nTARJETAS:\n");
        if (p.acta.tarjetas.isEmpty()) s.append("Sin tarjetas\n");
        for (Tarjeta t : p.acta.tarjetas) s.append(t.minuto).append("' ").append(t.tipo).append(" - ").append(t.jugador.nombre).append(" ").append(t.jugador.apellido).append("\n");
        if (p.finalizado) s.append("\nMarcador final: ").append(p.golesLocal).append(" - ").append(p.golesVisitante);
        JTextArea area = new JTextArea(s.toString(), 17, 42); area.setEditable(false); area.setFont(texto);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Acta del partido", JOptionPane.INFORMATION_MESSAGE);
    }
    private void vistaPosiciones() {
        contenido.add(titulo("★ Tabla de posiciones"), BorderLayout.NORTH);
        JPanel p = vertical();
        JComboBox<String> cats = new JComboBox<String>(new String[]{"Juvenil", "Senior", "Máster"});
        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT)); arriba.setBackground(fondo);
        arriba.add(new JLabel("Categoría: ")); arriba.add(cats);
        p.add(arriba, BorderLayout.NORTH);
        JPanel tablaPanel = new JPanel(new BorderLayout());
        tablaPanel.setBackground(fondo);
        p.add(tablaPanel, BorderLayout.CENTER);
        Runnable actualizar = () -> {
            ArrayList<Equipo> lista = new ArrayList<Equipo>();
            for (Equipo e : Main.equipos) if (e.categoria == cats.getSelectedIndex() + 1) lista.add(e);
            lista.sort((a,b) -> b.puntos != a.puntos ? Integer.compare(b.puntos,a.puntos) : Integer.compare(b.golesFavor-b.golesContra,a.golesFavor-a.golesContra));
            Object[][] datos = new Object[lista.size()][9];
            for (int i = 0; i < datos.length; i++) {
                Equipo e = lista.get(i);
                datos[i] = new Object[]{e.nombre,e.jugados,e.ganados,e.empatados,e.perdidos,e.golesFavor,e.golesContra,e.golesFavor-e.golesContra,e.puntos};
            }
            tablaPanel.removeAll();
            tablaPanel.add(desplazamiento(tabla(new String[]{"Equipo","PJ","G","E","P","GF","GC","DG","PTS"}, datos)), BorderLayout.CENTER);
            tablaPanel.revalidate(); tablaPanel.repaint();
        };
        cats.addActionListener(e -> actualizar.run()); actualizar.run();
        contenido.add(p, BorderLayout.CENTER);
    }
}
