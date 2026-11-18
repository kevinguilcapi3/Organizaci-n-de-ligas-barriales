public class Posicion {
    private String equipo;
    private String categoria;
    private int jugados;
    private int ganados;
    private int empatados;
    private int perdidos;
    private int golesFavor;
    private int golesContra;
    private int puntos;

    public Posicion(String equipo, String categoria) {
        this.equipo = equipo;
        this.categoria = categoria;
    }

    public String getEquipo() { return equipo; }
    public String getCategoria() { return categoria; }
    public int getJugados() { return jugados; }
    public int getGanados() { return ganados; }
    public int getEmpatados() { return empatados; }
    public int getPerdidos() { return perdidos; }
    public int getGolesFavor() { return golesFavor; }
    public int getGolesContra() { return golesContra; }
    public int getPuntos() { return puntos; }
    public int getDiferencia() { return golesFavor - golesContra; }

    public boolean sumarResultado(int favor, int contra) {
        if (favor < 0 || contra < 0 || favor > 30 || contra > 30) {
            return false;
        }
        jugados++;
        golesFavor += favor;
        golesContra += contra;
        if (favor > contra) {
            ganados++;
            puntos += 3;
        } else if (favor == contra) {
            empatados++;
            puntos++;
        } else {
            perdidos++;
        }
        return true;
    }
}
