/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import beans.StatusObject;
import com.google.common.net.InetAddresses;
import exception.IPException;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;

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

    public static StatusObject startConnection(String ip) {
        try {
            NetworkManager.openConnection(ip, 17494);
        } catch (IOException ex) {
            return new StatusObject("Problema di connessione al relè", true);
        } catch (IPException ex) {
            return new StatusObject(ex.getMessage(), true);
        }
        return new StatusObject("OK", false);
    }

    public static void accensione() {
        if (releSocket != null) {
            byte[] w = new byte[]{DIGITAL_ACTIVE_CODE, RELE_CODE, 0};
            NetworkManager.writeInSocket(w);
        }

    }

    public static void spegimento() {
        byte[] w = new byte[]{DIGITAL_INACTIVE_CODE, RELE_CODE, 0};
        NetworkManager.writeInSocket(w);
    }

    public static String output_states() {
        NetworkManager.writeInSocket(new byte[]{DIGITAL_GET_OUT_CODE});
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] data = NetworkManager.readInSocket(1);
        //System.out.println("Relay states: " + String.format("%8s", Integer.toBinaryString((data[0] & 0xFF))).replace(' ', '0'));
        return String.valueOf(data);
    }

    protected static class NetworkManager {

        protected static void openConnection(String ip, int port) throws IOException, IPException {
            if (InetAddresses.isInetAddress(ip)) {
                //inserire connessione al socket
                releSocket = new Socket(ip, port);
                inputStream = releSocket.getInputStream();
                outputStream = releSocket.getOutputStream();
            } else {
                new IPException(IPException.IP_ERROR_MESSAGE);
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
            byte[] data = new byte[2];

            //for (int x = 0; x < count; x++) {
            try {
                inputStream.read(data);
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException e) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, e);
            } catch (IndexOutOfBoundsException e) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, e);
            }
            //}

            return data;
        }
    }

}
