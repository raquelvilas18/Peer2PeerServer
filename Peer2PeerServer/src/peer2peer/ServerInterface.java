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
    
    public boolean iniciarSesion(ClientInterface callbackClientObject, String password) throws java.rmi.RemoteException;

    public void cerrarSesion( ClientInterface callbackClientObject)throws java.rmi.RemoteException;
    
    public String[] buscarPersona(String nombre)throws java.rmi.RemoteException;
    
    public boolean enviarPeticion(String emisor, String receptor)throws java.rmi.RemoteException;
    
    public void aceptarPeticion(String emisor, String receptor)throws java.rmi.RemoteException;
    
    public void rechazarPeticion(String emisor, String receptor)throws java.rmi.RemoteException;
    
    public boolean crearCuenta(String nombre, String password) throws java.rmi.RemoteException;
    
    public boolean eliminarCuenta(String nombre) throws java.rmi.RemoteException;
    
    public boolean actualizarPassword(String nombre, String nuevaPasswrod)throws java.rmi.RemoteException;
}
