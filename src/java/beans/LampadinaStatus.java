package beans;

/**
 * Classe beans per l'output della lampadina
 *
 * @author samuPC
 */
public class LampadinaStatus {

    private String id = "1";
    private String statusLampadina;

    public LampadinaStatus(String statusLampadina) {
        this.statusLampadina = statusLampadina;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusLampadina() {
        return statusLampadina;
    }

    public void setStatusLampadina(String statusLampadina) {
        this.statusLampadina = statusLampadina;
    }

}
