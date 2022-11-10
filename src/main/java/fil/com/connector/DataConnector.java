package fil.com.connector;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import fil.com.connector.utilitaire.ConnectWriter;

/**
 * Cette classe hérite d'un connecteur et implémente la faculté de lire. 
 * Elle permet de manipuler le canal de données en FTP.
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public class DataConnector extends Connector  implements ConnectWriter {
	
	private ServerSocket server;
	private int port;
	private OutputStream out;
	private BufferedOutputStream outStream;
	private BufferedInputStream inStream;
	private PrintWriter print;

	/**
	 * Démarre la connexion et instancie tous les éléments nécessaires à l'écriture
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @throws Erreur de connexion
	 */
	public DataConnector() throws IOException {
        super();
		this.server = new ServerSocket(0);
		this.port = this.server.getLocalPort();
	}
	
	/**
	 * Méthode d'écriture implémenté
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param message : Chaine de caractère à envoyer
	 * @throws Erreur de connexion
	 */
	public void write(String message) throws IOException {
		Connect();
		this.print.println(message);
		Disconnect();
	}
	
	/**
	 * Méthode d'envoi du contenu d'un fichier
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param fileStream : Stream du fichier à envoyer
	 * @throws Erreur de connexion ou de lecture du fichier
	 */
	public void sendFile(InputStream fileStream) throws IOException {
		Connect();
		byte[] buffer = new byte[512];
		int num;
		while ((num = fileStream.read(buffer)) > 0) {
			outStream.write(buffer, 0, num);
		}
		outStream.flush();
		Disconnect();
		fileStream.close();
	}
	
	/**
	 * Méthode de réception du contenu d'un fichier
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param fileOutput : Stream du fichier sur lequel écrire les données récupérées
	 * @throws Erreur de connexion ou de lecture du fichier
	 */
	public void getFile(OutputStream fileOutput) throws IOException {
		Connect();
		final byte[] buffer = new byte[512];
		int num;
		while ((num = inStream.read(buffer)) > 0) {
			fileOutput.write(buffer, 0, num);
		}
	}
	
	/**
	 * Méthode de connexion ou de reconnexion du canal de données
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @throws Erreur de connexion
	 */
	private void Connect() throws UnknownHostException, IOException {
		Disconnect();
		this.connexion = this.server.accept();
		//Ecriture
		this.out = this.connexion.getOutputStream();
		this.print = new PrintWriter(this.out, true);
		this.outStream = new BufferedOutputStream(this.connexion.getOutputStream());
		this.inStream = new BufferedInputStream(this.connexion.getInputStream());
	}
	
	/**
	 * Méthode de déconnexion du canal de données
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @throws Erreur de connexion
	 */
	public void Disconnect() throws IOException {
		if(connexion != null && connexion.isConnected()) {
			connexion.close();
			connexion = null;
		}
	}

	public int GetPort() { return this.port; }
	
}