package apitareas;
/* @Gustavo Apaza Huanca */
import java.sql.*;
import java.util.Scanner;
public class ApiTareas {
    static Scanner entrada = new Scanner(System.in);
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
        Aca creo variables para guardar valores que el usuario ingrese y para 
        ayudarme a verificar ciertos procesos
        */
        int opcion, idTarea, contadorDeFilas;
        String nombreTarea, descripcionTarea, estadoTarea = "";
        System.out.println(".:: APP TASK ::.\n" + 
                "\n.::MENU::."
                + "\n1. Mostrar Tareas"
                + "\n2. Ingresar Tarea"
                + "\n3. Eliminar Tarea"
                + "\n4. Modificar el estado de una Tarea"
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
                    estadoTarea = elegirEstado();
                    /*
                    Aca tomo los valores de cada variable y los paso a la base de datos
                    */
                    try{
                        //st = mt.con.createStatement();
                        String query = "insert into tarea(nombre,descripcion,estado)" + "values (?,?,?)";          
                        PreparedStatement ps = mt.con.prepareStatement(query);
                        ps.setString(1, nombreTarea);
                        ps.setString(2, descripcionTarea);
                        ps.setString(3, estadoTarea);
                        contadorDeFilas = ps.executeUpdate();

                        if(contadorDeFilas > 0 ){
                            System.out.println("Se regitro la nueva tarea exitosamente");
                        }
                    }catch(Exception error){
                        System.err.println("Error al ingresar los datos");
                    }
                    break;
                case 3:
                    System.out.print("Ingresar la id de la tarea a eliminar: ");
                    idTarea = entrada.nextInt();
                    try{           
                        //st = mt.con.createStatement();
                        String query = "DELETE FROM tarea WHERE id = ?";
                        PreparedStatement ps = mt.con.prepareStatement(query);

                        ps.setInt(1, idTarea);
                        contadorDeFilas = ps.executeUpdate();

                        if(contadorDeFilas > 0 ){
                            System.out.println("El registro se elimino exitosamente");
                        }
                         else{
                            System.out.println("No se borro el registro que eligio porque no existe la id que ingreso");
                        }
                    }catch(SQLException e){
                        System.out.println("ERROR " + e);
                    }
                    break;
                case 4:
                    System.out.print("Ingresar la id de la tarea a modificar: ");
                    idTarea = entrada.nextInt();
                    estadoTarea = elegirEstado();
                    try{           
                        //st = mt.con.createStatement();
                        String query = "UPDATE tarea SET estado = ? WHERE id = ?"; 
                        PreparedStatement ps = mt.con.prepareStatement(query);
                        ps.setString(1, estadoTarea);
                        ps.setInt(2, idTarea);           
                        contadorDeFilas = ps.executeUpdate();

                        if(contadorDeFilas > 0) {
                           System.out.println("Se realizo la modificacion exitosamente");
                        }else{
                           System.out.println("No se modifico ningun registro porque no existe la id que ingreso");
                        }
                    }
                    catch(SQLException e){
                        System.out.println("ERROR " + e);
                    }
                    break;
            }
            
            if(opcion == 0){
                /*
                Aca recien se cierra la conexion a la BD porque asi permito que el
                usuario interactue con las opciones del menu hasta que solicite 
                salir del menu o el programa
                */
                try{
                    mt.con.close();
                }catch(Exception e){
                    System.err.println("Error al cerrar la conexion con la BD");
                }
                System.out.println("\n================== Fin del programa ==================");
            }
        }while(opcion != 0);
    }
    ////////////////////////////////////////////////////////////////////////////
    // FUNCION
    ////////////////////////////////////////////////////////////////////////////
    //static: significa que el método pertenece a la clase Main y no a un objeto de la clase Main
    public static String elegirEstado(){
        int opcionEstado;
        String estadoTarea = "", estado1 = "realizado", estado2 = "en proceso", estado3 = "no realizado";
        System.out.print("Elija de la lista el estado de la tarea"
                            + "\n1. " + estado1
                            + "\n2. " + estado2
                            + "\n3. " + estado3
                            + "\nOpcion: ");
        opcionEstado = entrada.nextInt();

        while(opcionEstado < 1 || opcionEstado > 3){
            System.out.print("Eleccion Incorrecta!. Elija una opcion de la lista de estados: ");
            opcionEstado = entrada.nextInt();
        }

        switch (opcionEstado){
            case 1:
                estadoTarea = estado1;
                break;
            case 2:
                estadoTarea = estado2;
                break;
            case 3:
                estadoTarea = estado3;
                break;    
        }
        return estadoTarea;
    }
}

