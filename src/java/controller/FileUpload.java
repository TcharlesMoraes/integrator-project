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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import model.Dataset;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import services.facade.DatasetFacade;
import services.facade.ConnexaoFactory;

@Named("file")
@SessionScoped
public class FileUpload implements Serializable {

    /*@PersistenceContext(unitName = "prjIntegrator")
    private EntityManager em;*/
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
    private static final Pattern DATE = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
    private static final Pattern INT = Pattern.compile("\\d+(\\.\\d+)?");

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
        context.addMessage(null, new FacesMessage("O arquivo: "
                + event.getFile().getFileName()
                + " Foi enviado com sucesso"));
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

    public String submit() {
        this.processFile(nomeArquivo, this.fileLocation);
        this.dataset.setCaminho(this.fileLocation);
        this.dataset.setDatasetName(nomeArquivo);
        this.dataset.setDataDataset(getDataDataset());
        this.dataset.setAdicionadoEm(getDataInsercao());
        datasetFacde.create(dataset);
        dataset = new Dataset();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Dataset " + nomeArquivo + " Cadastrado com sucesso"));
        return "listDatasets?faces-redirect=true";
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file, UploadedFile arq)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[(int) arq.getSize()];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private static String makeSlug(String input) {

        if (input == null) {
            throw new IllegalArgumentException();
        }

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("_");
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

            Map<Integer, String> typeMap = new HashMap();

            String[] columns = csvReader.readNext();
            String[] firstData = csvReader.readNext();
            String ddlSql = "CREATE TABLE " + tableName + "(";
            for (int i = 0; i < columns.length; i++) {

                if (isNumericType(firstData[i])) {

                    ddlSql += String.format("\"%s\" numeric DEFAULT NULL", columns[i]);
                    typeMap.put(i, "numeric");
                } else if (isDateType(firstData[i])) {

                    ddlSql += String.format("\"%s\" DATE DEFAULT NULL", columns[i]);
                    typeMap.put(i, "date");
                } else {

                    ddlSql += String.format("\"%s\" varchar(300) DEFAULT NULL", columns[i]);
                    typeMap.put(i, "string");
                }

                if (i + 1 < columns.length) {
                    ddlSql += ",";
                }
            }
            ddlSql += ");";

            createTable(ddlSql);

            String insertSql = "";// = "INSERT INTO " + tableName + " VALUES (";
            int k = 0;
            for (Iterator<String[]> rowIterator = csvReader.iterator(); rowIterator.hasNext();) {

                insertSql += "INSERT INTO " + tableName + " VALUES (";
                String[] row = rowIterator.next();
                for (int i = 0; i < row.length; i++) {

                    switch (typeMap.get(i)) {
                        case "string":
                        case "date":
                            insertSql += String.format("'%s'", row[i]);
                            break;
                        default:
                            insertSql += row[i];
                    }
                    if (i + 1 < row.length) {
                        insertSql += ",";
                    }
                }
                insertSql += ");\n";
                if (k % 1000 == 0) {
                    insert(insertSql);
                    insertSql = "";
                }
                k++;

            }
            //insert(insertSql);
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insert(String insert) {
        Connection conn;
        PreparedStatement ps;
        try {
            conn = ConnexaoFactory.getConnection();
            ps = conn.prepareStatement(insert);
            ps.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void createTable(String create) {
        Connection conn;
        PreparedStatement ps;
        try {
            System.out.println(create);
            conn = ConnexaoFactory.getConnection();
            ps = conn.prepareStatement(create);
            ps.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private boolean isNumericType(String string) {
        return string.matches(FileUpload.INT.toString());

    }

    private boolean isDateType(String string) {

        return string.matches(FileUpload.DATE.toString());
    }
}
