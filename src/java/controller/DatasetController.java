package controller;

import java.util.Date;
import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@ManagedBean
@Named("dataset")
@RequestScoped
public class DatasetController {
    private String caminho, datasetName;
    private Date adicionadoEm, dataDataset;
    
    
    
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
