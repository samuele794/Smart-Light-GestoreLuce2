/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

public class StatusObject {

    private String message;
    private boolean exception;

    public StatusObject(String message, boolean exception) {
        this.message = message;
        this.exception = exception;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String error) {
        this.message = message;
    }

}
