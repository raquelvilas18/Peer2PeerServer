/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer;
import java.rmi.*;
import java.util.*;
/**
 *
 * @author raquel
 */
public interface ClientInterface extends java.rmi.Remote{
    public void nuevoAmigoConectado(ClientInterface amigoConectado) 
      throws java.rmi.RemoteException;
    public void nuevoAmigoDesconectado(ClientInterface amigoConectado) 
      throws java.rmi.RemoteException;
      
    public Vector getAmigos() throws java.rmi.RemoteException;
    public void setAmigos(Vector amigos) throws java.rmi.RemoteException;
    public String getNombre()throws java.rmi.RemoteException;

    
}
