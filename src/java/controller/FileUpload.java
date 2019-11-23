package controller;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Dataset;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import services.facade.DatasetFacade;

@ManagedBean
@Named("file")
@SessionScoped
public class FileUpload implements Serializable {

    @Inject
    private DatasetFacade datasetFacde;
    
    private String caminho = "c:\\csv\\";
    private String nomeArquivo;
    private Date dataInsercao = new Date();
    private Date dataDataset;
    private UploadedFile file;
    private Dataset dataset = new Dataset();
    private String fileLocation;
    
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    
    public String submeterArquivo() {
        return "fileUpload?faces-redirect=true";
    }
    
    @SuppressWarnings("RedundantStringToString")
    public void upload(FileUploadEvent event) throws IOException {
        
        file = event.getFile();
        this.fileLocation = caminho + event.getFile().getFileName().toString();
        System.out.println(fileLocation);
        File arq = new File(caminho + event.getFile().getFileName().toString());
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
        
        this.processFile(nomeArquivo, this.fileLocation);
        this.dataset.setCaminho(this.fileLocation);
        this.dataset.setDatasetName(nomeArquivo);
        this.dataset.setDataDataset(getDataDataset());
        this.dataset.setAdicionadoEm(getDataInsercao());
        datasetFacde.create(dataset);
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
  
    private static String makeSlug(String input) {
        
        if (input == null)
            throw new IllegalArgumentException();

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase();
    }
    
    private void processFile(String datasetName, String fileLocation) {
        try {

            String tableName = makeSlug(datasetName);

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(fileLocation));
            CSVReader csvReader = new CSVReader(reader);

            String[] columns = csvReader.readNext();
            String ddlSql = "CREATE TABLE " + tableName + "(";
            for (int i = 0; i < columns.length; i++) {
                
                ddlSql += columns[i];
                if (i + 1 < columns.length)
                    ddlSql += ",";
            }
            
            for (Iterator<String[]> rowIterator = csvReader.iterator(); rowIterator.hasNext();) {
                
                String insertSql = "INSERT INTO " + tableName + " VALUES (";
                String[] row = rowIterator.next();
                for (int i = 0; i < row.length; i++) {
                    
                    insertSql += row[i];
                    if (i + 1 < row.length)
                        insertSql += ",";
                }
                insertSql += ")";
                
                System.out.println(insertSql);
            }
            
            csvReader.close();	    
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
