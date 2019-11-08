package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Dataset;

@ManagedBean
@Named("dataset")
@RequestScoped
public class DatasetController {
    private String caminho, datasetName;
    private Date adicionadoEm, dataDataset;
    private List<Dataset> datasets;
    public String redirect(){
        return "listDatasets?faces-redirect=true";
    }
    public List<Dataset> getDatasets(){
        this.datasets = datasets = new ArrayList<Dataset>();
        return this.datasets;
    }

    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
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
