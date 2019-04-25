/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer;

import java.util.Vector;

/**
 *
 * @author raquel
 */
public interface ServerInterface extends java.rmi.Remote{
    
    public void iniciarSesion(ClientInterface callbackClientObject) throws java.rmi.RemoteException;

    public void cerrarSesion( ClientInterface callbackClientObject)throws java.rmi.RemoteException;
    
    public boolean buscarPersona(String nombre)throws java.rmi.RemoteException;
    
    public void enviarPeticion(String nombre)throws java.rmi.RemoteException;
    
}
