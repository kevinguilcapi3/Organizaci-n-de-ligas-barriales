import java.util.ArrayList;

public class Acta {
    int idActa;
    ArrayList<Gol> goles = new ArrayList<Gol>();
    ArrayList<Tarjeta> tarjetas = new ArrayList<Tarjeta>();
    Acta(int idActa) { this.idActa = idActa; }
}
