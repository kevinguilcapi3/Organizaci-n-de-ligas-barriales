import java.util.ArrayList;

public class Equipo {
    String nombre;
    int categoria, jugados, ganados, empatados, perdidos, golesFavor, golesContra, puntos;
    Jugador capitan;
    ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    Equipo(String nombre, int categoria) { this.nombre = nombre; this.categoria = categoria; }
    String nombreCategoria() {
        if (categoria == 1) return "Juvenil";
        if (categoria == 2) return "Senior";
        return "Master";
    }
}
