package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Scanner;
import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@Named("file")
@RequestScoped
public class FileUpload implements Serializable {

    private String caminho = "c:\\csv\\";
    private String nomeArquivo;
    private Date dataInsercao = new Date();
    private Date dataDataset;
    private UploadedFile file;
    public String submeterArquivo() {
        return "fileUpload?faces-redirect=true";
    }
    
    public void upload(FileUploadEvent event) throws IOException {
        this.file = event.getFile();
        File arq = new File(caminho + event.getFile().getFileName());
        arq.createNewFile();
        copyInputStreamToFile(event.getFile().getInputstream(), arq, this.file);
        FacesContext context = FacesContext.getCurrentInstance(); 
        context.addMessage(null, new FacesMessage("O arquivo: " + 
                event.getFile().getFileName() + 
                " Foi enviado com sucesso"));
        
        
    }

    public Date getDataInsercao() {
        return dataInsercao;
    }

    public void setDataInsercao(Date dataInsercao) {
        this.dataInsercao = dataInsercao;
    }

    public Date getDataDataset() {
        return dataDataset;
    }

    public void setDataDataset(Date dataDataset) {
        this.dataDataset = dataDataset;
    }
    
    
    
    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    
    
    public void submit(){
        FacesContext context = FacesContext.getCurrentInstance(); 
        context.addMessage(null, new FacesMessage("Dataset "+ nomeArquivo +" Cadastrado com sucesso"));
    }
    
    private static void copyInputStreamToFile(InputStream inputStream, File file, UploadedFile arq)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[(int) arq.getSize()];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

}
