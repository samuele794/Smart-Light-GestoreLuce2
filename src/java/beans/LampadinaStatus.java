package beans;

/**
 * Classe beans per l'output della lampadina
 * @author samuPC
 */
public class LampadinaStatus {

    private String statusLampadina;

    public LampadinaStatus(String statusLampadina) {
        this.statusLampadina = statusLampadina;
    }
    
    public String getStatusLampadina() {
        return statusLampadina;
    }

    public void setStatusLampadina(String statusLampadina) {
        this.statusLampadina = statusLampadina;
    }

}
