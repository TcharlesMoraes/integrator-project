package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
public class Dataset implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String caminho;
    
    private String datasetName;
  
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date adicionadoEm;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataDataset;

    public Dataset(long id, String caminho, String datasetName, Date adicionadoEm, Date dataDataset) {
        this.id = id;
        this.caminho = caminho;
        this.datasetName = datasetName;
        this.adicionadoEm = adicionadoEm;
        this.dataDataset = dataDataset;
    }

    public Dataset() {
    }
    
    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public Date getAdicionadoEm() {
        return adicionadoEm;
    }

    public void setAdicionadoEm(Date adicionadoEm) {
        this.adicionadoEm = adicionadoEm;
    }

    public Date getDataDataset() {
        return dataDataset;
    }

    public void setDataDataset(Date dataDataset) {
        this.dataDataset = dataDataset;
    }
    
    
}
