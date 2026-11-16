 import java.util.Scanner;

public class SistemaRolesBasico {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcionRol = 0;

        while (opcionRol != 3) {
            
            System.out.println("   SISTEMA DE ROLES - MENÚ ");
            System.out.print(" Ingrese una opcion (1-3): ");
            System.out.println("1. Entrar como Administrador");
            System.out.println("2. Entrar como Usuario");
            System.out.println("3. Salir");
            

            opcionRol = scanner.nextInt();
            scanner.nextLine();

            if (opcionRol == 1) {
                menuAdmin(scanner);
            } else if (opcionRol == 2) {
                menuUsuario(scanner);
            } else if (opcionRol == 3) {
                System.out.println("Saliendo...");
            } else {
                System.out.println("Error, porfavor seleccione entre 1, 2 o 3");
            }
        }

        scanner.close();
    }

    public static void menuAdmin(Scanner scanner) {
        int opcion = 0;

        while (opcion != 3) {
            System.out.println("\n Panel Administrador");
            System.out.print("Elige una opción: ");
            System.out.println("1. Ver lista de usuarios");
            System.out.println("2. Agregar un usuario nuevo");
            System.out.println("3. Volver al menú principal");
            

            opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 1) {
                System.out.println("-> Usuarios en el sistema: Ana, Carlos, María.");
            } else if (opcion == 2) {
                System.out.print("Escribe el nombre del nuevo usuario: ");
                String nombre = scanner.nextLine();
                System.out.println("-> ¡Éxito! El usuario '" + nombre + "' fue registrado.");
            } else if (opcion == 3) {
                System.out.println("Volviendo al menú principal...");
            } else {
                System.out.println("Opción no válida.");
            }
        }
    }

    public static void menuUsuario(Scanner scanner) {
        int opcion = 0;

        while (opcion != 3) {
            System.out.println("PANEL DE USUARIO ESTÁNDAR ");
            System.out.println("1. Ver mis tareas pendientes");
            System.out.println("2. Agregar una tarea nueva");
            System.out.println("3. Volver al menú principal");
            System.out.print("Elige una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 1) {
                System.out.println("-> Tarea 1: Revisar documentación asignada.");
                System.out.println("-> Tarea 2: Enviar el reporte semanal.");
            } else if (opcion == 2) {
                System.out.print("Escribe tu nueva tarea: ");
                String tarea = scanner.nextLine();
                System.out.println("-> ¡Tarea '" + tarea + "' agregada a tu lista!");
            } else if (opcion == 3) {
                System.out.println("Volviendo al menú principal...");
            } else {
                System.out.println("Opción no válida.");
            }
        }
    }
}
