public class Jugador extends Persona {
    boolean capitan;
    int amarillas;
    String estado;
    Jugador(Persona persona, boolean capitan) {
        super(persona.nombre, persona.apellido, persona.edad, persona.cedula, persona.telefono);
        this.capitan = capitan;
        this.amarillas = 0;
        this.estado = "Habilitado";
    }
}
