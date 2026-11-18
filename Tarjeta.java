public class Tarjeta {
    int idTarjeta, minuto;
    String tipo;
    Jugador jugador;
    Tarjeta(int idTarjeta, String tipo, int minuto, Jugador jugador) {
        this.idTarjeta = idTarjeta; this.tipo = tipo;
        this.minuto = minuto; this.jugador = jugador;
    }
}
