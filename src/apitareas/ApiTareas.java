package apitareas;
/* @Gustavo Apaza Huanca */
import java.sql.*;
import java.util.Scanner;
public class ApiTareas {
    /*
    Aca se prepara la conexion con la BD
    */
    Connection con;
    public ApiTareas(){
        try{
	    Class.forName("com.mysql.cj.jdbc.Driver");
            //Modificar segun su configuracion.
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tareas_bd?serverTimezone=UTC","root","12345");
            System.out.println("Se conecto a la Base de datos");
        } catch(Exception e) {
            System.err.println("Error " + e);
        }
    }
    public static void main(String[] args) {
        /*
        Aca creo un objeto de la clase ApiTareas y creo 2 variables para realizar
        las consultas sobre la BD
        */
        ApiTareas mt = new ApiTareas();
        Statement st;
        ResultSet rs;
        /*
        Aca creo un objeto de la clase Scanner y creo variables para guardar valores
        que el usuario ingrese
        */
        Scanner entrada = new Scanner(System.in);
        int opcion;
        String nombreTarea, descripcionTarea, estadoTarea;
        System.out.println(".:: APP TASK ::.\n" + 
                "\n.::MENU::."
                + "\n1. Mostrar Tareas"
                + "\n2. Ingresar Tarea"
                + "\n3. Eliminar Tarea"
                + "\n0. Salir del menu");
        
        do{
            System.out.print("\nElija una opcion del menu"
                            + "\nOpcion: ");
            opcion = entrada.nextInt();
            
            switch(opcion){
                case 1:
                    System.out.println("Listado de tareas"
                                    + "\nN° de tarea || Nombre || Descripcion || Estado");
                    try{
                        //Modificar Segun su modelo datos
                        st = mt.con.createStatement();
                        rs = st.executeQuery("SELECT * FROM tarea");

                        while(rs.next()){
                           System.out.println(rs.getInt("id") + " || " + rs.getString("nombre") + " || " + rs.getString("descripcion") + " || " + rs.getString("estado"));
                        }
                    }catch (Exception e){
                        System.err.println("ERROR AL OBTENER LOS DATOS");
                    }
                    break;
                case 2:
                    entrada.nextLine();
                    System.out.print("Ingrese el nombre de la tarea: ");
                    nombreTarea = entrada.nextLine().toLowerCase();
                    System.out.print("Ingrese la descripcion de la tarea: ");
                    descripcionTarea = entrada.nextLine().toLowerCase();
                    System.out.print("Ingrese el estado de la tarea: ");
                    estadoTarea = entrada.nextLine().toLowerCase();
                    /*
                    Aca tomo los valores de cada variable y los paso a la base de datos
                    */
                    try{
                        String query = "insert into tarea(nombre,descripcion,estado)" + "values (?,?,?)";          
                        PreparedStatement ps = mt.con.prepareStatement(query);
                        ps.setString(1, nombreTarea);
                        ps.setString(2, descripcionTarea);
                        ps.setString(3, estadoTarea);
                        ps.execute();
                    }catch(Exception error){
                        System.err.println("Error al ingresar los datos");
                    }
                    break;
                case 3:
                    System.out.println("Solicitar nombre de la tarea a eliminar");
                    break;
            }
            
            if(opcion == 0){
                /*
                Aca recien se cierra la conexion la BD porque asi permito que el
                usuario interactue con las opciones del menu hasta que solicite 
                salir del menu o el programa
                */
                try{
                    mt.con.close();
                }catch(Exception e){
                    System.err.println("Error al cerrar la conexion con la BD");
                }
                System.out.println("\n========= Fin del programa =========");
            }
        }while(opcion != 0);
    }
}
