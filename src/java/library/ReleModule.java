/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import com.google.common.net.InetAddresses;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuPC
 */
public class ReleModule {

    private final static byte DIGITAL_ACTIVE_CODE = 0x20;
    private final static byte DIGITAL_INACTIVE_CODE = 0x21;
    private final static byte DIGITAL_GET_OUT_CODE = 0x24;
    private final static byte LOG_OUT_CODE = 0x7B;

    private static Socket releSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public static void openConnection(String ip, int port) {
        if (InetAddresses.isInetAddress(ip)) {

            try {
                //inserire connessione al socket
                releSocket = new Socket(ip, port);
                inputStream = releSocket.getInputStream();
                outputStream = releSocket.getOutputStream();
            } catch (IOException ex) {
                Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           //connessione non riuscita
        }
    }

    public static void closeConnection() {
        try {
            outputStream.write(new byte[]{LOG_OUT_CODE});
            outputStream.close();
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ReleModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
