package fil.com.connector;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import fil.com.connector.utilitaire.ConnectReader;
import fil.com.connector.utilitaire.ConnectWriter;

/**
 * Cette classe hérite d'un connecteur et implémente la faculté de lire et d'écrire. 
 * Elle permet de manipuler le canal de commandes en FTP.
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public class CommandeConnector extends Connector implements ConnectReader,ConnectWriter{
	
	private InputStream in;
	private InputStreamReader inReader;
	private BufferedReader reader;
	private OutputStream out;
	private PrintWriter print;
	private Socket connexion;
	
	/**
	 * Démarre la connexion et instancie tous les éléments nécessaires à la lecture et à l'écriture
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param connexion : Socket de connexion utilisé pour le canal de commandes
	 * @throws Erreur de connexion
	 */
	public CommandeConnector(Socket connexion) throws IOException {
		this.connexion=connexion;
		try {
			this.in = this.connexion.getInputStream();
			this.inReader = new InputStreamReader(this.in);
			this.reader = new BufferedReader(inReader);
			//Ecriture
			this.out = this.connexion.getOutputStream();
			this.print = new PrintWriter(this.out, true);
		} catch (NullPointerException e) {
			
		} catch( Exception e){

		}
	}
	
	/**
	 * Méthode de lecture implémenté
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @return Chaine de caractère réponse du serveur FTP
	 * @throws Erreur de connexion
	 */
	public String read() throws IOException {
		String fromFTP = this.reader.readLine();
		return fromFTP;
	}
	
	/**
	 * Méthode d'écriture implémenté
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param message : Message chaine de caractères à envoyer
	 */
	public void write(String message) {
		this.print.println(message);
	}
	
	/**
	 * Déconnecte la connexion
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @throws Erreur au niveau de la Socket
	 */
	public void Disconnect() throws IOException {
		if(connexion != null && connexion.isConnected()) {
			connexion.close();
			connexion = null;
		}
	}
}