package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Scanner;
import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@Named("file")
@RequestScoped
public class FileUpload implements Serializable {

    private String caminho = "c:\\csv\\";
    private String csv;

    public String submeterArquivo() {
        return "fileUpload?faces-redirect=true";
    }

    public void upload(FileUploadEvent event) throws IOException {
        
        File file = new File(caminho + event.getFile().getFileName());
        file.createNewFile();
        copyInputStreamToFile(event.getFile().getInputstream(), file);
        
        /*Scanner s = new Scanner(event.getFile().getInputstream());
        String cont = "";

        while (s.useDelimiter(";").hasNext()) {
            cont += s.next() + " ";
        }
        s.close();
        System.out.println(cont);*/

    }

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

}
