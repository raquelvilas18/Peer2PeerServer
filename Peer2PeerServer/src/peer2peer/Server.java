/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author raquel
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;
        try {
            System.out.println("Enter the RMIregistry port number:");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);
            startRegistry(RMIPortNum);
            System.out.println("holi");
            ServerImpl exportedObj = new ServerImpl();
            registryURL = "rmi://localhost:" + portNum + "/peer2peer";
            Naming.rebind(registryURL,  exportedObj);
            System.out.println("Callback Server ready.");
        }// end try
        catch (Exception re) {
            System.out.println("Exception in HelloServer.main: " + re);
        } // end catch
    } // end main

    private static void startRegistry(int RMIPortNum) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
        } catch (RemoteException e) {
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
        }
    }
}
