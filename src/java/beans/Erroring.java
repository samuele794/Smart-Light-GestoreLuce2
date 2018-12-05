/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 * Classe beans per gli errori
 *
 * @author samuPC
 */
public class Erroring {

    private String id = "1";
    private String message;

    public Erroring(String message) {
        setError(message);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getError() {
        return message;
    }

    public void setError(String message) {
        this.message = message;
    }

}
