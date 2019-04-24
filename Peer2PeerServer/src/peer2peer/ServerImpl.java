/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 *
 * @author raquel
 */
public class ServerImpl  extends UnicastRemoteObject implements ServerInterface {
    
    private Vector clientesActivos;
    
     public ServerImpl() throws RemoteException {
        super();
        clientesActivos = new Vector();
    }
     
    public Vector iniciarSesion(ClientInterface callbackClientObject) throws java.rmi.RemoteException{
        Vector amigosConectados = new Vector();
        
        // Añadir el cliente que inicio sesion a la lista de clientes activos
        if (!(clientesActivos.contains(callbackClientObject))) {
            clientesActivos.addElement(callbackClientObject);
            System.out.println("El cliente ha iniciado sesion");
        }
        //OBTENER LISTA DE AMIGOS DEL USUARIO+ .nuevoAmigoConectado()
        return amigosConectados;
    }

    public void cerrarSesion( ClientInterface callbackClientObject)throws java.rmi.RemoteException{
        
    }
    
    public ClientInterface buscarPersona(String nombre){
        return null;
    }
    
}
