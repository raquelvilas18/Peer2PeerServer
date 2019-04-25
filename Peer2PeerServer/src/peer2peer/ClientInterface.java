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
public interface ClientInterface extends java.rmi.Remote{
      public void addAmigoConectado(ClientInterface amigoConectado) throws java.rmi.RemoteException;
    public void nuevoAmigoConectado(ClientInterface amigoConectado) throws java.rmi.RemoteException;
    public void nuevoAmigoDesconectado(ClientInterface amigoDesconectado) throws java.rmi.RemoteException; 
    public  String[] getAmigos() throws java.rmi.RemoteException;
    public void setAmigos( String[]amigos) throws java.rmi.RemoteException;
//    public ClientInterface[] getAmigosConectados();
//    public ArrayList<String> getPeticionesAmistad();
    public void setAmigosConectados(HashMap<String, ClientInterface> amigosConectados);
//    public void setPeticionesAmistad(ArrayList<String> peticionesAmistad);
    public String getNombre() throws java.rmi.RemoteException; 
   /* public void setNombre(String nombre);
    public void recibirMensaje(String mensaje);*/
    
}
