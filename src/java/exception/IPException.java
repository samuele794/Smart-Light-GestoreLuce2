/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 * Classe per l'eccezzione da ip non valido
 * @author samuPC
 */
public class IPException extends Exception{
    
    public static String IP_ERROR_MESSAGE = "IP non trovato o non corretto";

    public IPException(String message) {
        super(message);
    }
}
