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
    
    
//    Este método comrpueba el nombre y contraseña de usuario. Si es correcto:
//        -Añade el objeto cliente a la lista de usuarios actv¡ivos que mantiene el servidor
//        -Setea al objeto cliente su lista de amigos
//        -Setea al objeto cliente la lista (de objetos clientes) de sus amigos activos
//        -Notifica a aquellos amigos que esten conectados que este usuario ha iniciado sesion
//        -Devuelve true si el usuario y contraseña son correctos y false de lo contrario
    public boolean iniciarSesion(ClientInterface callbackClientObject, String password) throws java.rmi.RemoteException;

//    Este metodo elimina al objeto cliente de la lista de clientes activos que tiene el servidor
//    -recorre la lista de sus amigos y notifica a los que esten conectados que este ha cerrado sesion (para que estos lo eliminen de su lista de amigos conectados)
    public void cerrarSesion( ClientInterface callbackClientObject)throws java.rmi.RemoteException;
     
//    Este método consulta en la BD aquellos usuarios cuyo nombre coincida con el patron dado menos:
//        -Excepto aquellos que ya sean amigos del usuario
//        -Excepto aquellos a los que ya les haya enviado una peticion
//        -Excepto el propio usuario
    public String[] buscarPersona(String usuario,String nombre)throws java.rmi.RemoteException; 
    
//    Este metodo almacena en la BD la peticion de amistad del emisor al receptor
//            -Si el receotor esta conectado; le notifica que le han enviado una peticion
    public boolean enviarPeticion(String emisor, String receptor)throws java.rmi.RemoteException;
    
//    Este método elimina la peticion de amistad de la BD del emisor al receptor
//            -Si existe, tambien elimina la peticion de amistad del receptor al emisor
//            -Añade a la lista de amigos del emisor al receptor
//            -Añade a la lista de amigos del receptor al emisor
//            -Si el emisor de la peticion de amiistad está conectado, se le notifica que han aceptado su peticion de amistad            
    public void aceptarPeticion(String emisor, String receptor)throws java.rmi.RemoteException;
    
//    Este metodo elimina la peticion del emisor al receptor de la BD
//            -Si el emisor de la peticion esta en linea, se le notifica que se ha rechazado su peticion
    public void rechazarPeticion(String emisor, String receptor)throws java.rmi.RemoteException;
    
//    Este método introduce en la BD el nuevo usuario
//        -Devuelve true si se introduce con exito
//        -Devuelve false si ya existia un usuario con ese nombre
    public boolean crearCuenta(String nombre, String password) throws java.rmi.RemoteException;
    
//    Este metodo elimina de la BD la cuenta dada
//            -Devuelve true si lo hace de forma correcta
//            -Devuelve false si no existia ningun usuario con ese nombre
    public boolean eliminarCuenta(String nombre) throws java.rmi.RemoteException;
    
//    Este metodo permite cambiar el valor de la contraseña de un usuario en la BD
//            -Devuelve true si lo hace de forma correcta
    public boolean actualizarPassword(String nombre, String nuevaPasswrod)throws java.rmi.RemoteException;
    
//    Este metodo consulta en la BD las peticiones de amistad actuales del cliente dado
//            -Vuelve a setear las peticiones de amistad del cliente
    public void acualizarPeticiones(ClientInterface ClientObject) throws java.rmi.RemoteException;
    
//    Este metodo consulta en la BD los amigos actuales de un cliente dado
//            -Vuelve a setear los amigos en el objecto cliente
    public void acualizarAmigos(ClientInterface ClientObject) throws java.rmi.RemoteException;
}
