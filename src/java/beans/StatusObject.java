/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

public class StatusObject {

    private String message;    //messaggio, può essere d'errore o di OK
    private boolean exception;	//controllo se il messaggio è di eccezzione  

    public StatusObject(String message, boolean exception) {
        this.message = message;
        this.exception = exception;
    }

    /**
     * Ritorna è stata scaturia un eccezzione
     * @return 
     */
    public boolean isException() {
        return exception;
    }

    /**
     * Setta se è stata scaturita un eccezzione
     * @param exception 
     */
    public void setException(boolean exception) {
        this.exception = exception;
    }
    
    /**
     * Metodo per ottenere il messaggio
     * @return 
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Metodo per settare il messaggio
     * @param error 
     */
    public void setMessage(String error) {
        this.message = message;
    }		//setta il messaggio

}
