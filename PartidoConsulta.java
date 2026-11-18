public class PartidoConsulta {
    private String categoria;
    private String local;
    private String visitante;
    private String fecha;
    private int golesLocal;
    private int golesVisitante;
    private boolean finalizado;

    public PartidoConsulta(String categoria, String local, String visitante, String fecha) {
        this.categoria = categoria;
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        finalizado = false;
    }

    public String getCategoria() { return categoria; }
    public String getLocal() { return local; }
    public String getVisitante() { return visitante; }
    public String getFecha() { return fecha; }
    public int getGolesLocal() { return golesLocal; }
    public int getGolesVisitante() { return golesVisitante; }
    public boolean estaFinalizado() { return finalizado; }

    public boolean finalizar(int golesLocal, int golesVisitante) {
        if (finalizado || golesLocal < 0 || golesVisitante < 0
                || golesLocal > 30 || golesVisitante > 30) {
            return false;
        }
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        finalizado = true;
        return true;
    }

    public String toString() {
        String marcador = "PENDIENTE";
        if (finalizado) {
            marcador = golesLocal + " - " + golesVisitante;
        }
        return fecha + " | " + categoria + " | " + local + " vs "
                + visitante + " | " + marcador;
    }
}
