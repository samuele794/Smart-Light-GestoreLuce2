/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

public class StatusObject {

    private String message;    //messaggio
    private boolean exception;	//eccezzione 

    public StatusObject(String message, boolean exception) {
        this.message = message;
        this.exception = exception;
    }			//costruttore

    public boolean isException() {
        return exception;
    }			//ritorna l'eccezzione

    public void setException(boolean exception) {
        this.exception = exception;
    }			//setta l'eccezzione
    
    public String getMessage() {
        return message;
    }			//ritorna il messaggio

    public void setMessage(String error) {
        this.message = message;
    }		//setta il messaggio

}
