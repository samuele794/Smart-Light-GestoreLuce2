/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 * Classe beans per gli errori
 * @author samuPC
 */
public class Erroring {

    private String error;
    
    public Erroring(String error){
        setError(error);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
