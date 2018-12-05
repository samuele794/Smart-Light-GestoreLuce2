package beans;

/**
 * Classe beans per l'output della lampadina
 *
 * @author samuPC
 */
public class LampadinaStatus {

    private String id = "1";
    private String status;

    public LampadinaStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusLampadina() {
        return status;
    }

    public void setStatusLampadina(String statusLampadina) {
        this.status = status;
    }

}
