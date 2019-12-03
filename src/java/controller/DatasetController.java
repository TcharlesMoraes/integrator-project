package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Dataset;
import services.facade.DatasetFacade;
import services.facade.SelectDataset;

@ManagedBean
@Named("dataset")
@SessionScoped
public class DatasetController implements Serializable {

    private SelectDataset jdbc = new SelectDataset();
    private String caminho, datasetName;
    private Date adicionadoEm, dataDataset, dataInicio, dataFim;
    @Inject
    DatasetFacade datasetFacade;
    private List<Dataset> datasets;
    private Dataset dataset = new Dataset();
    private List<List<String>> dadosTabela;
    private List<String> cabecalhos;
    private Map<String, List<String>> typeMap;

    private String media = "";
    private String soma = "";
    private String group = "";
    private String date = "";

    public String redirect() {
        return "listDatasets?faces-redirect=true";
    }

    public List<Dataset> getDatasets() {
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

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
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

    public List<List<String>> getDadosTabela() {
        return dadosTabela;
    }

    public List<String> getCabecalhos() {
        return cabecalhos;
    }

    public String delete(Dataset d) {
        datasetFacade.remove(d);
        dataset = new Dataset();
        return "listDatasets?faces-redirect=true";
    }

    public String viewDataset(Dataset dt) {
        dadosTabela = new ArrayList<>();
        cabecalhos = new ArrayList<>();
        dataset = dt;
        carregaDadosTabela();
        return "viewDataset?faces-redirect=true";
    }

    public SelectDataset getJdbc() {
        return jdbc;
    }

    public void setJdbc(SelectDataset jdbc) {
        this.jdbc = jdbc;
    }

    public List<String> getNumbers() {
        return typeMap.get("numeric");
    }

    public List<String> getStrings() {
        return typeMap.get("character varying");
    }

    public List<String> getDates() {
        return typeMap.get("date");
    }

    public String getSoma() {
        return soma;
    }

    public void setSoma(String soma) {
        this.soma = soma;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String soma) {
        this.group = soma;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String soma) {
        this.media = soma;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String soma) {
        this.date = soma;
    }

    public void updateTable() {
        String select = "SELECT ";
        String avg = "";
        String sum = "";
        String between = "";
        String groupBy = "";
        List<String> bet = cabecalhos;
        cabecalhos = new ArrayList();
        if (media != null && media != "") {
            cabecalhos.add(media);
            avg = "AVG(\"" + media + "\") AS \"" + media + "\"";
        }
        if (soma != null && soma != "") {
            cabecalhos.add(soma);
            sum = "SUM(\"" + soma + "\") AS \"" + soma + "\"";
        }
        if (group != null && group != "") {
            cabecalhos.add(group);
            groupBy = "GROUP BY \"" + group + "\"";
        }
        if (date != null && date != "") {
            between = "WHERE \"" + date + "\" BETWEEN '"
                    + dataInicio.toLocaleString() + "' AND '" + dataFim.toLocaleString() + "'";
        }

        if (avg != "" || sum != "") {
            if (sum != "" && avg == "") {
                select += sum;
            } else if (avg != "" && sum == "") {
                select += avg;
            } else {
                select += sum + ", " + avg;
            }

            if (between != "") {
                if (groupBy != "") {
                    select += ", \"" + group + "\" FROM " + dataset.getDatasetName();
                } else {
                    select += "FROM " + dataset.getDatasetName();
                }
                select += " " + between;
                select += " " + groupBy;
            } else {
                if (groupBy != "") {
                    select += ", \"" + group + "\" FROM " + dataset.getDatasetName() + " " + groupBy;
                }else {
                    select += " FROM " + dataset.getDatasetName();
                }
            }
        }

        if (between != "" && avg == "" && sum == "") {

            select += "* FROM " + dataset.getDatasetName();
            select += " " + between;
            cabecalhos = jdbc.getCampos();

        }
        System.out.println(select);
        jdbc.executeQuery(select, cabecalhos);
        dadosTabela = jdbc.getConsulta();
    }

    private void carregaDadosTabela() {
        jdbc.getColunas(dataset.getDatasetName());
        jdbc.getTypes(dataset.getDatasetName());
        jdbc.findAll(dataset.getDatasetName());
        cabecalhos = new ArrayList();
        cabecalhos = jdbc.getCampos();
        typeMap = jdbc.getTypeMap();
        dadosTabela = new ArrayList();
        dadosTabela = jdbc.getConsulta();
    }

}
