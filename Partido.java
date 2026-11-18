public class Partido {
    int id, hora, golesLocal, golesVisitante;
    String cancha, fecha, estado;
    Equipo local, visitante;
    boolean finalizado;
    Acta acta;
    Partido(int id, Equipo local, Equipo visitante, String cancha, String fecha, int hora) {
        this.id = id; this.local = local; this.visitante = visitante;
        this.cancha = cancha; this.fecha = fecha; this.hora = hora;
        this.estado = "Pendiente"; this.finalizado = false;
        this.acta = new Acta(id);
    }
}
