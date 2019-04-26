/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raquel
 */
public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

    private HashMap<String, ClientInterface> clientesActivos;
    private java.sql.Connection conexion;

    public ServerImpl() throws RemoteException {
        super();
        this.initConexion();
        clientesActivos = new HashMap<>();
    }

    public boolean iniciarSesion(ClientInterface usuario, String password) throws java.rmi.RemoteException {
        if (comprobarPassword(usuario.getNombre(), password)) {
            // Añadir el cliente que inicio sesion a la lista de clientes activos
            if (!(clientesActivos.containsKey(usuario.getNombre()))) {
                clientesActivos.put(usuario.getNombre(), usuario);
                System.out.println(usuario.getNombre() + " ha iniciado sesion");
            }
            //OBTENER LISTA DE AMIGOS DEL USUARIO+ .nuevoAmigoConectado()
            String[] amigos = obtenerAmigos(usuario.getNombre());
            usuario.setAmigos(amigos);
            obtenerNotificarAmigosConectados(amigos, usuario);
            usuario.setPeticionesAmistad(obtenerPeticiones(usuario.getNombre()));
            return true;
        } else {
            return false;
        }
    }

    public void cerrarSesion(ClientInterface usuario) throws java.rmi.RemoteException {

        //ELIMINAR DE LA LISTA DE USUARIOS ACTIVOS DEL SERVIDOR
        if (clientesActivos.containsKey(usuario.getNombre())) {
            clientesActivos.remove(usuario.getNombre());
        }

        //NOTIFICAR A LOS AMIGOS DEL USUARIO QUE ESTEN CONECTADOS QUE EL USUARIO HA CERRADO SESION (los quitan de su lista de amigos activs)
        String[] amigos = obtenerAmigos(usuario.getNombre());
        notificarAmigosDesconexion(amigos, usuario.getNombre());
        System.out.println(usuario.getNombre() + " ha cerrado sesion");
    }

    //Consulta en la base todos los usuarios que contengan los caracteres buscados en su nombre
    public String[] buscarPersona(String nombre) {
        PreparedStatement stm;
        ArrayList<String> coincidenciasBusqueda = new ArrayList<>();
        try {
            stm = conexion.prepareStatement("SELECT * FROM usuarios WHERE nombre like ? ");
            stm.setString(1, "%" + nombre + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                coincidenciasBusqueda.add(rs.getString("nombre"));
            }
            //Convertir arrayList a array
            String[] resultadoArray = new String[coincidenciasBusqueda.size()];
            resultadoArray = coincidenciasBusqueda.toArray(resultadoArray);
            return resultadoArray;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void enviarPeticion(String emisor, String receptor) throws java.rmi.RemoteException {
        //Pensar cuando el destinatario esta conectado
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("INSERT peticiones VALUES(?,?)");
            stm.setString(1, emisor);
            stm.setString(2, receptor);
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void aceptarPeticion(String emisor, String receptor) {
        eliminarPeticionBD(emisor, receptor);
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("INSERT amigos VALUES(?,?)");
            stm.setString(1, emisor);
            stm.setString(2, receptor);
            stm.executeUpdate();

            stm = conexion.prepareStatement("INSERT amigos VALUES(?,?)");
            stm.setString(2, emisor);
            stm.setString(1, receptor);
            stm.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rechazarPeticion(String emisor, String receptor) {
        eliminarPeticionBD(emisor, receptor);
    }

    public boolean crearCuenta(String nombre, String password) {
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("INSERT usuarios VALUES(?, ?)");
            stm.setString(1, nombre);
            stm.setString(2, password);
            stm.executeUpdate();
            System.out.println(nombre + " ha creado una cuenta");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean eliminarCuenta(String nombre) {
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("DELETE FROM usuarios WHERE nombre=?");
            stm.setString(1, nombre);
            stm.executeUpdate();
            System.out.println(nombre + " ha eliminado su cuenta");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean actualizarPassword(String nombre, String nuevaPasswrod) {
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("UPDATE usuarios SET  contrasenha=? WHERE nombre=?");
            stm.setString(1, nuevaPasswrod);
            stm.setString(2, nombre);
            stm.executeUpdate();
            System.out.println(nombre + " ha actualizado su contraseña");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //------------------METODOS AUXILIARES ------------------------------//

    //Conexion con la Base de datos del sistema
    private void initConexion() {
        try {
            //Datos de conexion con la bd
            Properties credenciales = new Properties();
            String gestor = "mysql";
            String servidor = "localhost";
            String puerto = "3306";
            String baseDatos = "peer2peer";
            //Datos del usuario de la bd
            credenciales.setProperty("user", "Peer2peer");
            credenciales.setProperty("password", "Peer2peer");

            //Conexion
            Class.forName("com.mysql.jdbc.Driver");
            this.conexion = java.sql.DriverManager.getConnection("jdbc:" + gestor + "://" + servidor + ":" + puerto + "/" + baseDatos, "Peer2peer", "Peer2peer");
        } catch (Exception e) {
            System.out.println("Imposible conectar con la Base de datos");
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //Consulta en la base de datos de los amigos de un usuario dado
    private String[] obtenerAmigos(String usuario) {
        ArrayList<String> amigos = new ArrayList<String>();
        String amigo;
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("SELECT amigo FROM amigos WHERE usuario=?");
            stm.setString(1, usuario);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                amigos.add(rs.getString("amigo"));
            }

            //Convertir arrayList a array
            String[] amigosArray = new String[amigos.size()];
            amigosArray = amigos.toArray(amigosArray);
            return amigosArray;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Recorre la lista de amigos del cliente; devuelve referencias a todos los conectados y a estos les notifica que se ha conectado
    private void obtenerNotificarAmigosConectados(String[] amigosTotales, ClientInterface usuario) {
        try {
            //Recorre la lista de amigos de usuarios
            for (String amigo : amigosTotales) {
                //Si el amigo esta conectado lo añade a la lista de amigos conectados y notifica a este amigo que el usuario ha iniciado sesion
                if (this.clientesActivos.containsKey(amigo)) {
                    usuario.addAmigoConectado(clientesActivos.get(amigo));
                    clientesActivos.get(amigo).nuevoAmigoConectado(usuario);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Recorre la lista de amigos del usuario; y de los que estén conectados les notifica que el usuario ha cerrado sesion
    private void notificarAmigosDesconexion(String[] amigos, String usuario) {
        try {
            for (String amigo : amigos) {
                if (this.clientesActivos.containsKey(amigo)) {
                    clientesActivos.get(amigo).nuevoAmigoDesconectado(usuario);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Consulta en la BD las peticiones de amistad que tiene pendientes el usuario
    private String[] obtenerPeticiones(String usuario) {
        ArrayList<String> peticiones = new ArrayList<String>();
        String emisor;
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("SELECT emisor FROM peticiones WHERE receptor=?");
            stm.setString(1, usuario);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                peticiones.add(rs.getString("emisor"));
            }
            //Convertir arrayList a array
            String[] peticionesArray = new String[peticiones.size()];
            peticionesArray = peticiones.toArray(peticionesArray);
            return peticionesArray;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean comprobarPassword(String usuario, String password) {
        PreparedStatement stm;
        ResultSet rs;
        try {
            stm = conexion.prepareStatement("SELECT * FROM usuarios WHERE nombre=? AND contrasenha=?");
            stm.setString(1, usuario);
            stm.setString(2, password);
            rs = stm.executeQuery();
            return (rs.next());
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void eliminarPeticionBD(String emisor, String receptor) {
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("DELETE FROM peticiones WHERE emisor=? AND receptor=?");
            stm.setString(1, emisor);
            stm.setString(2, receptor);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
