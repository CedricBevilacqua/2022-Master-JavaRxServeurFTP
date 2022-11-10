package fil.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MockDataManager extends DataManager{

    private boolean add =false;
    private String dossier;
    private boolean in =false;


    public MockDataManager(String cheminServeur) {
        super(cheminServeur);
       
    }

    @Override
    public InputStream GetFile(String filename) throws IOException {
      
        return super.GetFile(filename);
    }

    @Override
    public String GetLocation() {
        
        return super.GetLocation();
    }

    @Override
    public void GoIn(String folderName) {
        if(folderName.equals("/projet-compil/isi-tp1-droits")){
            this.in=true;
        }

       
        super.GoIn(folderName);
        
        
    }

    @Override
    public void GoUp() {
        
        super.GoUp();
    }

    @Override
    public ArrayList<String> List() {
        ArrayList<String> fileList = new ArrayList<String>();
        if(in){
            fileList.add("-rw-r--r--  1794 13 fév 17:33 ex.c");
        }
        else{
            fileList.add("-rw-r--r--  194 26 jan 00:00 admin.sh");
            fileList.add("drwxr-xr-x  1384 12 fév 15:26 isi-tp1-droits");
        }

        if(this.add){
            fileList.add("drwxr-xr-x  1384 12 fév 15:26 "+this.dossier);
        }
        return fileList;
    }

    @Override
    public void NewFolder(String folderName) throws Exception {
        this.add = true;
        this.dossier=folderName;
    }

    @Override
    public OutputStream PutFile(String filename) throws IOException {
       
        return super.PutFile(filename);
    }

    @Override
    public void Remove(String toRemove) throws Exception {
        this.add=false;
        this.dossier=null;
    }

    @Override
    public void Rename(String fileSource, String fileDest) throws IOException {
       
        super.Rename(fileSource, fileDest);
    }


    
}
