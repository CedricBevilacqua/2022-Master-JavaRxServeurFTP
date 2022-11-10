package fil.com;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Cette classe permet d'effectuer des manipulations sur le système du fichier du serveur
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public class DataManager {
	protected String cheminServeur;
	protected ArrayList<String> cheminActuel = new ArrayList<String>();
	
	/**
	 * Initialise l'instance avec le chemin du serveur
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 */
	public DataManager(String cheminServeur) {
		this.cheminServeur = cheminServeur;
	}
	
	/**
	 * Fourni la liste des fichiers et dossiers présents à l'emplacement actuel
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @return Liste de chaines de caractères représentant chacune un fichier ou un dossier
	 */
	public ArrayList<String> List() {
		ArrayList<String> fileList = new ArrayList<String>();
		File dir  = new File(this.cheminServeur + GetLocation());
	    File[] liste = dir.listFiles();
	    for(File item : liste) {
	    	String itemLine = "";
	    	if(item.isFile()) { itemLine += '-'; } 
	        else if(item.isDirectory()) { itemLine += 'd'; } 
	    	for(int boucle = 0; boucle < 3; boucle++) {
	    		if(item.canRead()) { itemLine += 'r'; }
	    		else { itemLine += '-'; }
	    		if(item.canWrite()) { itemLine += 'w'; }
	    		else { itemLine += '-'; }
	    		if(item.canExecute()) { itemLine += 'x'; }
	    		else { itemLine += '-'; }
	    	}
	    	itemLine += "    1 997      997           810 Feb 01 03:03 ";
	    	itemLine += item.getName();
	    	fileList.add(itemLine);
	     }
		return fileList;
	}
	
	/**
	 * Fourni l'adresse actuelle relatif par rapport à la racine du serveur
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @return Adresse actuelle
	 */
	public String GetLocation() {
		String location = "";
		if(cheminActuel.size() == 0) {
			location = "/";
		}
		for(String folder : cheminActuel) {
			location += '/' + folder;
		}
		return location;
	}
	
	/**
	 * Avance dans un dossier de l'arborescence
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param folderName : Nom du dossier dans lequel avancer
	 */
	public void GoIn(String folderName) {
		if(folderName.charAt(0) == '/') {
			folderName = folderName.substring(1);
			this.cheminActuel.clear();
		}
		String[] path = folderName.split("/");
		for(String folder : path) {
			System.out.println(folder);
			cheminActuel.add(folder);
		}
	}
	
	/**
	 * Revient en arrière dans l'arborescence
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 */
	public void GoUp() {
		if(cheminActuel.size() != 0) {
			cheminActuel.remove(cheminActuel.size() - 1);
		}
	}

	/**
	 * Crée un nouveau dossier
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param folderName : Nom du dossier à créer
	 */
	public void NewFolder(String folderName) throws Exception {
		File dir  = new File(this.cheminServeur + GetLocation() + '/' + folderName);
		Boolean answer = dir.mkdir();
		if(!answer) { throw new Exception(); }
	}

	/**
	 * Renome un fichier ou un dossier
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param fileSource : Nom du fichier à renomer
	 * @param fileDest : Nouveau nom à donner au fichier
	 */
	public void Rename(String fileSource, String fileDest) throws IOException {
		if(fileDest.charAt(0) == '/') {
			Files.move(Paths.get(this.cheminServeur + GetLocation() + '/' + fileSource), Paths.get(this.cheminServeur + fileDest));
		} else {
			Files.move(Paths.get(this.cheminServeur + GetLocation() + '/' + fileSource), Paths.get(this.cheminServeur + GetLocation() + '/' + fileDest));
		}
	}

	/**
	 * Renome un fichier ou un dossier
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param fileSource : Nom du fichier à renomer
	 * @param fileDest : Nouveau nom à donner au fichier
	 */
	public void Remove(String toRemove) throws Exception {
		File fileToRemove  = new File(this.cheminServeur + GetLocation() + '/' + toRemove);
		Boolean answer = fileToRemove.delete();
		if(!answer) { throw new Exception(); }
	}
	
	/**
	 * Récupère un stream sur un fichier
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param filename : Nom du fichier sur lequel récupérer le stream
	 * @return Stream sortant du fichier indiqué
	 */
	public InputStream GetFile(String filename) throws IOException{
		File file = new File(this.cheminServeur + GetLocation() + '/' + filename);
		InputStream dataStream = new FileInputStream(file);
		return dataStream;
	}
	
	/**
	 * Récupère un stream sur un fichier
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param filename : Nom du fichier sur lequel récupérer le stream
	 * @return Stream entrant du fichier indiqué
	 */
	public OutputStream PutFile(String filename) throws IOException{
		File file = new File(this.cheminServeur + GetLocation() + '/' + filename);
		file.createNewFile();
		OutputStream dataIn = new FileOutputStream(file);
		return dataIn;
	}

}