/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer;

import java.io.ObjectOutputStream;
import java.rmi.*;
import java.util.*;

/**
 *
 * @author raquel
 */
public interface ClientInterface extends java.rmi.Remote {
    
    
    //Añade el objeto Cliente a la lista de amigos conectados del cliente (para setear al principio los amigos conectados
    public void addAmigoConectado(ClientInterface amigoConectado) throws java.rmi.RemoteException;

    //Añade el objeto cliente a la lista de amigos conectados y lo notifica de que se acaba de conectar (cuando este cliente esta activo y un amigo se conecta)
    public void nuevoAmigoConectado(ClientInterface amigoConectado) throws java.rmi.RemoteException;

    public void nuevoAmigoDesconectado(String amigoDesconectado) throws java.rmi.RemoteException;

    public String[] getAmigos() throws java.rmi.RemoteException;

    public void setAmigos(String[] amigos) throws java.rmi.RemoteException;

    public void setPeticionesAmistad(String[] peticionesAmistad) throws java.rmi.RemoteException;

    public String getNombre() throws java.rmi.RemoteException;
    

    public void recibirMensaje(String mensaje, String nombreEmisor) throws java.rmi.RemoteException;

}
