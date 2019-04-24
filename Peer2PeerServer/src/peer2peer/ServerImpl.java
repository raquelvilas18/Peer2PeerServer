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

    public void iniciarSesion(ClientInterface usuario) throws java.rmi.RemoteException {
        Vector amigosConectados = new Vector();

        // Añadir el cliente que inicio sesion a la lista de clientes activos
        if (!(clientesActivos.containsKey(usuario.getNombre()))) {
            clientesActivos.put(usuario.getNombre(), usuario);
            System.out.println(usuario.getNombre() + " ha iniciado sesion");
        }
        //OBTENER LISTA DE AMIGOS DEL USUARIO+ .nuevoAmigoConectado()
        ArrayList<String> amigos = obtenerAmigos(usuario.getNombre());
        usuario.setAmigos(amigos);
        usuario.setAmigosConectados(obtenerNotificarAmigosConectados(amigos, usuario));
        usuario.setPeticionesAmistad(obtenerPeticiones(usuario.getNombre()));
    }

    public void cerrarSesion(ClientInterface ClientObject) throws java.rmi.RemoteException {
        //Vector amigos = ClientObject.getAmigos();
    }

    public ClientInterface buscarPersona(String nombre) {
        return null;
    }

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
            this.conexion = java.sql.DriverManager.getConnection("jdbc:" + gestor + "://" + servidor + ":" + puerto + "/" + baseDatos, credenciales);
        } catch (Exception e) {
            System.out.println("Imposible conectar con la Base de datos");
        }
    }

    //Consulta en la base de datos de los amigos de un usuario dado
    private ArrayList<String> obtenerAmigos(String usuario) {
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
            return amigos;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Recorre la lista de amigos del cliente; devuelve referencias a todos los conectados y a estos les notifica que se ha conectado
    private HashMap<String, ClientInterface> obtenerNotificarAmigosConectados(ArrayList<String> amigosTotales, ClientInterface usuario) {
        HashMap<String, ClientInterface> amigosConectados = new HashMap<>();
        try {
            for (String amigo : amigosTotales) {
                if (this.clientesActivos.containsKey(amigo)) {
                    amigosConectados.put(amigo, clientesActivos.get(amigo));
                    clientesActivos.get(amigo).nuevoAmigoConectado(usuario);

                }
            }
            return amigosConectados;
        } catch (RemoteException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    //Consulta en la BD las peticiones de amistad que tiene pendientes el usuario
    private ArrayList<String> obtenerPeticiones(String usuario){
         ArrayList<String> peticiones = new ArrayList<String>();
        String emisor;
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("SELECT emisor FROM peticiones WHERE receptor=?");
            stm.setString(1, usuario);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                peticiones.add(rs.getString("amigo"));
            }
            return peticiones;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
