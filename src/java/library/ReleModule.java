/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import com.google.common.net.InetAddresses;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuPC
 */
public class ReleModule {

    private final static byte RELE_CODE = 0x01;

    private final static byte DIGITAL_ACTIVE_CODE = 0x20;
    private final static byte DIGITAL_INACTIVE_CODE = 0x21;
    private final static byte DIGITAL_GET_OUT_CODE = 0x24;
    private final static byte LOG_OUT_CODE = 0x7B;

    private static Socket releSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public static void output_states() {
        NetworkManager.writeInSocket(new byte[]{DIGITAL_GET_OUT_CODE});
        byte[] data = NetworkManager.readInSocket(1);
        //System.out.println("Relay states: " + String.format("%8s", Integer.toBinaryString((data[0] & 0xFF))).replace(' ', '0'));
        String.format("%8s", Integer.toBinaryString((data[0] & 0xFF))).replace(' ', '0');
    }

    public static void startConnection(String ip) {
        NetworkManager.openConnection(ip, 17494);
    }

    public static String accensione() {
        byte[] w = new byte[]{DIGITAL_ACTIVE_CODE, RELE_CODE, 0};
        NetworkManager.writeInSocket(w);

        
        //non corretto
        byte[] result = NetworkManager.readInSocket(1);
        
        StringBuilder builder = new StringBuilder();
        
        for (byte r : result) {
            String re = String.valueOf(r);
            builder.append(Integer.parseInt(re, 16));
        }
        
        return builder.toString();
    }

    public static String spegimento() {
        byte[] w = new byte[]{DIGITAL_INACTIVE_CODE, RELE_CODE, 0};
        NetworkManager.writeInSocket(w);

        //non corretto
        byte[] result = NetworkManager.readInSocket(1);
        
        StringBuilder builder = new StringBuilder();
        
        for (byte r : result) {
            String re = String.valueOf(r);
            builder.append(Integer.parseInt(re, 16));
        }
        
        return builder.toString();
    }

    protected static class NetworkManager {

        protected static void openConnection(String ip, int port) {
            if (InetAddresses.isInetAddress(ip)) {
                try {
                    //inserire connessione al socket
                    releSocket = new Socket(ip, port);
                    inputStream = releSocket.getInputStream();
                    outputStream = releSocket.getOutputStream();
                } catch (IOException ex) {
                    //eccezzione in connessione
                    Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //connessione non riuscita
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
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        protected static byte[] readInSocket(int count) {
            byte[] data = new byte[32];
            try {
                inputStream.read(data, 0, count);
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
