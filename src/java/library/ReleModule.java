/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import beans.LampadinaStatus;
import beans.StatusObject;
import com.google.common.net.InetAddresses;
import exception.IPException;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuPC
 */
public class ReleModule {
    
    private static String ip = "192.168.0.200";

    private final static byte RELE_CODE = 0x01;

    private final static byte DIGITAL_ACTIVE_CODE = 0x20;
    private final static byte DIGITAL_INACTIVE_CODE = 0x21;
    private final static byte DIGITAL_GET_OUT_CODE = 0x24;
    private final static byte LOG_OUT_CODE = 0x7B;

    private static Socket releSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public static StatusObject startConnection() {
        try {
            NetworkManager.openConnection(ip, 17494);
            return new StatusObject("OK", false);
        } catch (IOException ex) {
            return new StatusObject("Problema di connessione al rele", true);
        } catch (IPException ex) {
            return new StatusObject(ex.getMessage(), true);
        }
        
        
    }

    public static void accensione() {
        if (releSocket != null) {
            byte[] w = new byte[]{DIGITAL_ACTIVE_CODE, RELE_CODE, 0};
            NetworkManager.writeInSocket(w);
            NetworkManager.readAvot();
        }

    }

    public static void spegimento() {
        byte[] w = new byte[]{DIGITAL_INACTIVE_CODE, RELE_CODE, 0};
        NetworkManager.writeInSocket(w);
        NetworkManager.readAvot();
    }

    //1: rele 1 acceso
    //2: rele 2 acceso
    //3: rele 1+2 acceso
    public static LampadinaStatus output_states() {
        NetworkManager.writeInSocket(new byte[]{DIGITAL_GET_OUT_CODE});
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] data = NetworkManager.readInSocket(1);

        if (((int) data[0]) == 0) {
            return new LampadinaStatus("0");
        } else {
            //rele 1 accesso
            return new LampadinaStatus("1");
        }
    }
    
    public static boolean isConnected(){
        if(releSocket != null){
            return releSocket.isConnected();
        }else{
            return false;
        }
    }

    protected static class NetworkManager {

        protected static void openConnection(String ip, int port) throws IOException, IPException {
            if (InetAddresses.isInetAddress(ip)) {
                //inserire connessione al socket
                releSocket = new Socket(ip, port);
                inputStream = releSocket.getInputStream();
                outputStream = releSocket.getOutputStream();
            } else {
                throw new IPException(IPException.IP_ERROR_MESSAGE);
            }
        }

        protected static void closeConnection() {
            try {
                outputStream.write(new byte[]{LOG_OUT_CODE});
                outputStream.close();
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        protected static void writeInSocket(byte msg[]) {
            try {
                outputStream.write(msg);
                outputStream.flush();
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            
        }

        protected static void readAvot(){
            try {
                Thread.sleep(100);
                inputStream.read(new byte[1]);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        protected static byte[] readInSocket(int count) {
            byte[] data = new byte[1];

            try {
                inputStream.read(data);
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException e) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, e);
            } catch (IndexOutOfBoundsException e) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, e);
            }

            return data;
        }
    }

}
