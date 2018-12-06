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
    
    private static String ip = "192.168.0.200"; //Indirizzo ip rele

    private final static byte RELE_CODE = 0x01; //codice della porta 1 del rele

    private final static byte DIGITAL_ACTIVE_CODE = 0x20;       //codice per richiedere accensione rele
    private final static byte DIGITAL_INACTIVE_CODE = 0x21;     //codice per richiedere spegnimento rele
    private final static byte DIGITAL_GET_OUT_CODE = 0x24;      //codice per richiedere l'output
    private final static byte LOG_OUT_CODE = 0x7B;              //codice per il logout in caso di uso di password

    private static Socket releSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    
    /**
     * Metodo per attivare la connessione con il rele
     * @return 
     */
    public static StatusObject startConnection() {
        try {
            NetworkManager.openConnection(ip, 17494); //porta di accesso al rele
            return new StatusObject("OK", false);
        } catch (IOException ex) {
            return new StatusObject("Problema di connessione al rele", true);
        } catch (IPException ex) {
            return new StatusObject(ex.getMessage(), true);
        }
        
        
    }
    /**
     * Comando per accendere il rele
     */
    public static void accensione() {
        if (releSocket != null) {
            byte[] w = new byte[]{DIGITAL_ACTIVE_CODE, RELE_CODE, 0};
            NetworkManager.writeInSocket(w);
            NetworkManager.readAvot();
        }

    }
    /**
     * Metoto per dare un solo impulso al rele. La risoluzione di impulso è di 100ms (quindi 10 = 1000ms = 1s)
     * @param time tempo di impulso
     */
    public static void pulseOn(int time) {
        if (releSocket != null) {
            byte[] w = new byte[]{DIGITAL_ACTIVE_CODE, RELE_CODE, (byte)time};
            NetworkManager.writeInSocket(w);
            NetworkManager.readAvot();
        }

    }
    
    /**
     * Comando per spegnere il rele
     */
    public static void spegimento() {
        byte[] w = new byte[]{DIGITAL_INACTIVE_CODE, RELE_CODE, 0};
        NetworkManager.writeInSocket(w);
        NetworkManager.readAvot();
    }

    //1: rele 1 acceso
    //2: rele 2 acceso
    //3: rele 1+2 acceso
    
    /**
     * Comando per ottenere lo stato attuale del rele 1
     * @return 
     */
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
    
    /**
     * Controllo se siamo connessi al rele
     * @return 
     */
    public static boolean isConnected(){
        if(releSocket != null){
            return releSocket.isConnected();
        }else{
            return false;
        }
    }
    
    /**
     * Classe base per gestire la connessione con il socket, leggere e scrivere
     */
    protected static class NetworkManager {

        /**
         * Metodo per aprire la connessione con il socket
         * @param ip indirizzo ip del rele
         * @param port porta finale del rele
         * @throws IOException Connessione non istaurata con successo
         * @throws IPException Ip con formato non corretto
         */
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
        
        /**
         * Metodo per chiedere la connessione con il socket
         */
        protected static void closeConnection() {
            try {
                outputStream.write(new byte[]{LOG_OUT_CODE});
                outputStream.close();
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /**
         * Metodo per scrivere sul socket
         * @param msg array di byte da scrivere nel socket
         */
        protected static void writeInSocket(byte msg[]) {
            int tentative = 0;
            try {
                outputStream.write(msg);
                outputStream.flush();
            } catch (IOException ex) {
               ReleModule.startConnection();
                try {
                    outputStream.write(msg);
                    outputStream.flush();
                } catch (IOException ex1) {
                    Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex1);
                }
                
            }            
        }
        
        /**
         * Metodo per una lettura a vuoto dell'output.
         * Da usare dopo un'azione di switch per l'accensione o lo spegnimento
         */
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
        
        /**
         * Metodo per leggere i dati in arrivo dal socket
         * @param count numero di bite da leggere
         * @return array di byte ricevuti
         */
        protected static byte[] readInSocket(int count) {
            byte[] data = new byte[count];

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
