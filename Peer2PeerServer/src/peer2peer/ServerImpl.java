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

    private HashMap<String,ClientInterface> clientesActivos;
    private java.sql.Connection conexion;

    public ServerImpl() throws RemoteException {
        super();
        this.initConexion();
        clientesActivos = new HashMap<>();
        //
    }

    public void iniciarSesion(ClientInterface ClientObject) throws java.rmi.RemoteException {
        Vector amigosConectados = new Vector();

        // Añadir el cliente que inicio sesion a la lista de clientes activos
        if (!(clientesActivos.containsKey(ClientObject.getNombre()))) {
            clientesActivos.put(ClientObject.getNombre(),ClientObject);
            System.out.println(ClientObject.getNombre() + " ha iniciado sesion");
        }
        //OBTENER LISTA DE AMIGOS DEL USUARIO+ .nuevoAmigoConectado()
        this.

    }

    public void cerrarSesion(ClientInterface ClientObject) throws java.rmi.RemoteException {
        Vector amigos = ClientObject.getAmigos();
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
    private HashMap<String, ClientInterface> obtenerAmigos(String usuario) {
        HashMap<String, ClientInterface> amigos = new HashMap<String, ClientInterface>();
        String amigo;
        PreparedStatement stm;
        try {
            stm = conexion.prepareStatement("SELECT amigo FROM amigos WHERE usuario=?");
            stm.setString(1, usuario);
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                amigo = rs.getString("amigo");
                if(this.clientesActivos.containsKey(amigo)){
                    amigos.put(amigo, clientesActivos.get(amigo));
                }
            }
            return amigos;
        } catch (SQLException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }

}
