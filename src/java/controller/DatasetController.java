package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Dataset;
import services.facade.DatasetFacade;

@ManagedBean
@Named("dataset")
@RequestScoped
public class DatasetController {
    private String caminho, datasetName;
    private Date adicionadoEm, dataDataset;
    @Inject
    DatasetFacade datasetFacade;
    private List<Dataset> datasets;
    private Dataset dataset;
    public String redirect(){
        return "listDatasets?faces-redirect=true";
    }
    public List<Dataset> getDatasets(){
        this.datasets = datasetFacade.findAll();
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

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
    
    
}
