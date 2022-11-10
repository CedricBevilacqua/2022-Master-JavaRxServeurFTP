package fil.com;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import fil.com.connector.*;

/**
 * Cette classe gère une connexion avec un client en FTP
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public class FTPServer implements Runnable {
	protected CommandeConnector commandes;
	protected DataConnector datas;
	protected DataManager dataManager;
	protected String username;
	protected String password;
	
	protected String fileToRename;
	protected Boolean usernameAccepted;
	protected Boolean passwordAccepted;
	
	/**
	 * Initialise tous les attributs à partir des paramètres reçus
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param connexion : Socket de connexion utilisé pour le canal de données
	 * @param servPath : Emplacement du serveur pour le DataManager
	 * @param username : Nom de l'utilisateur demandé pour se connecter
	 * @param password : Mot de passe demandé pour se connecter
	 * @throws Erreur avec le client
	 */
	public FTPServer(Socket connexion, String servPath, String username, String password) throws Exception {
		this.commandes = new CommandeConnector(connexion);
		this.dataManager = new DataManager(servPath);
		this.username = username;
		this.usernameAccepted = false;
		this.password = password;
		this.passwordAccepted = false;
	}

	/**
	 * Démarre la connexion avec le serveur et écoute toutes les commandes qu'il peut faire pour demander leur traitement
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @throws Erreur avec le client
	 */
	protected void StartConnexion() throws Exception {
		commandes.write("220 FTP server (vsftpd)");
		String content;
		while(true) {
			content = commandes.read().toString();
			System.out.println(content);
			if(this.usernameAccepted && this.passwordAccepted) {
				answerProtocolCommands(content);
			} else {
				authProtocolCommands(content);
			}
		}
	}
	
	/**
	 * Point d'entrée du thread, démarre toute la gestion des commandes avec le client et gère les erreurs qui en découlent
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 */
	public void run() {
		try {
			StartConnexion();
		} catch (Exception e) {
			System.out.println("Connexion terminée");
			try {
				Disconnect();
			} catch (Exception e1) {}
		}
		return;
	}
	
	/**
	 * Déconnecte tous les connecteurs avec le client
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @throws Erreur avec le client
	 */
	public void Disconnect() throws IOException {
		datas.Disconnect();
		commandes.Disconnect();
	}
	
	/**
	 * Gère toutes les commandes liées à l'authentification sur le serveur
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param commande : Commande à traiter
	 * @throws Erreur avec le client
	 */
	public void authProtocolCommands(String commande) throws Exception {
		if(commande.charAt(0) == 'A' && commande.charAt(1) == 'U' && commande.charAt(2) == 'T' && commande.charAt(3) == 'H'
				&& commande.charAt(4) == ' ' && commande.charAt(5) == 'T' && commande.charAt(6) == 'L' && commande.charAt(7) == 'S') {
			commandes.write("530 Please login with USER and PASS.");
		} else if(commande.charAt(0) == 'A' && commande.charAt(1) == 'U' && commande.charAt(2) == 'T' && commande.charAt(3) == 'H'
				&& commande.charAt(4) == ' ' && commande.charAt(5) == 'S' && commande.charAt(6) == 'S' && commande.charAt(7) == 'L') {
			commandes.write("530 Please login with USER and PASS.");
		} else if(commande.charAt(0) == 'U' && commande.charAt(1) == 'S' && commande.charAt(2) == 'E' && commande.charAt(3) == 'R') {
			int maxPointeur;
			if(commande.substring(5).length() > this.username.length()) {
				maxPointeur = commande.substring(5).length();
			} else {
				maxPointeur = this.username.length();
			}
			for(int pointeur = 0; pointeur < maxPointeur; pointeur++) {
				if(commande.substring(5).charAt(pointeur) != this.username.charAt(pointeur)) {
					throw new Exception();
				}
			}
			commandes.write("331 Please specify the password.");
			this.usernameAccepted = true;
		} else if(commande.charAt(0) == 'P' && commande.charAt(1) == 'A' && commande.charAt(2) == 'S' && commande.charAt(3) == 'S') {
			if(!this.usernameAccepted) {
				throw new Exception();
			}
			int maxPointeur;
			if(commande.substring(5).length() > this.password.length()) {
				maxPointeur = commande.substring(5).length();
			} else {
				maxPointeur = this.password.length();
			}
			for(int pointeur = 0; pointeur < maxPointeur; pointeur++) {
				if(commande.substring(5).charAt(pointeur) != this.password.charAt(pointeur)) {
					throw new Exception();
				}
			}
			commandes.write("230 Login successful.");
			this.passwordAccepted = true;
		}
	}
	
	/**
	 * Gère toutes les commandes sur le serveur
	 *
	 * @author Cédric Bevilacqua, Sema Altinkaynak
	 * @param commande : Commande à traiter
	 * @throws Erreur avec le client
	 */
	public void answerProtocolCommands(String commande) throws Exception {
		if(commande.charAt(0) == 'S' && commande.charAt(1) == 'Y' && commande.charAt(2) == 'S' && commande.charAt(3) == 'T') {
			commandes.write("215 UNIX Type: L8");
		} else if(commande.charAt(0) == 'F' && commande.charAt(1) == 'E' && commande.charAt(2) == 'A' && commande.charAt(3) == 'T') {
			commandes.write("211 End");
		} else if(commande.charAt(0) == 'P' && commande.charAt(1) == 'W' && commande.charAt(2) == 'D') {
			commandes.write("257 \"" + dataManager.GetLocation() + "\" is the current directory");
		} else if(commande.charAt(0) == 'L' && commande.charAt(1) == 'I' && commande.charAt(2) == 'S' && commande.charAt(3) == 'T') {
			commandes.write("150 Here comes the directory listing.");
			String fileList = "";
			for(String fileName : this.dataManager.List()) {
				fileList += fileName;
				fileList += '\n';
			}
			datas.write(fileList);
			commandes.write("226 Directory send OK.");
		} else if(commande.charAt(0) == 'T' && commande.charAt(1) == 'Y' && commande.charAt(2) == 'P' && commande.charAt(3) == 'E'
				 && commande.charAt(4) == ' ' && commande.charAt(5) == 'I') {
			commandes.write("200 Switching to Binary mode.");
		}	else if(commande.charAt(0) == 'T' && commande.charAt(1) == 'Y' && commande.charAt(2) == 'P' && commande.charAt(3) == 'E'
			&& commande.charAt(4) == ' ' && commande.charAt(5) == 'A') {
	   		commandes.write("200 Switching to ASCII mode.");
		} else if(commande.charAt(0) == 'E' && commande.charAt(1) == 'P' && commande.charAt(2) == 'S' && commande.charAt(3) == 'V') {
			this.datas = new DataConnector();
			commandes.write("229 Entering Extended Passive Mode (|||" + this.datas.GetPort() + "|)");
		} else if(commande.charAt(0) == 'C' && commande.charAt(1) == 'W' && commande.charAt(2) == 'D') {
			String parameter = commande.substring(4);
			this.dataManager.GoIn(parameter);
			this.commandes.write("250 Directory successfully changed.");
		} else if(commande.charAt(0) == 'C' && commande.charAt(1) == 'D' && commande.charAt(2) == 'U' && commande.charAt(3) == 'P') {
			this.dataManager.GoUp();
			this.commandes.write("250 Directory successfully changed.");
		} else if(commande.charAt(0) == 'M' && commande.charAt(1) == 'K' && commande.charAt(2) == 'D') {
			String folderName = commande.substring(4);
			this.dataManager.NewFolder(folderName);
			this.commandes.write("257 \"" + folderName + "\" : The directory was successfully created");
		} else if(commande.charAt(0) == 'R' && commande.charAt(1) == 'N' && commande.charAt(2) == 'F' && commande.charAt(3) == 'R') {
			this.fileToRename = commande.substring(5);
			this.commandes.write("350 RNFR accepted - file exists, ready for destination");
		} else if(commande.charAt(0) == 'R' && commande.charAt(1) == 'N' && commande.charAt(2) == 'T' && commande.charAt(3) == 'O') {
			if(this.fileToRename != null) {
				this.dataManager.Rename(this.fileToRename, commande.substring(5));
				this.fileToRename = null;
				this.commandes.write("250 File or directory successfully renamed or moved");
			}
		} else if(commande.charAt(0) == 'R' && commande.charAt(1) == 'M' && commande.charAt(2) == 'D') {
			this.dataManager.Remove(commande.substring(4));
			this.commandes.write("250 The directory was successfully removed");
		} else if(commande.charAt(0) == 'D' && commande.charAt(1) == 'E' && commande.charAt(2) == 'L' && commande.charAt(3) == 'E') {
			this.dataManager.Remove(commande.substring(5));
			this.commandes.write("250 The file was successfully removed");
		} else if(commande.charAt(0) == 'R' && commande.charAt(1) == 'E' && commande.charAt(2) == 'T' && commande.charAt(3) == 'R') {
			InputStream fileDataStream = this.dataManager.GetFile(commande.substring(5));
			this.commandes.write("150 Accepted data connection");
			this.datas.sendFile(fileDataStream);
			this.commandes.write("226 File successfully transferred");
		} else if(commande.charAt(0) == 'S' && commande.charAt(1) == 'T' && commande.charAt(2) == 'O' && commande.charAt(3) == 'R') {
			OutputStream fileInDataStream = this.dataManager.PutFile(commande.substring(5));
			this.commandes.write("150 Accepted data connection");
			this.datas.getFile(fileInDataStream);
			this.commandes.write("226 File successfully transferred");
		} else if(commande.charAt(0) == 'Q' && commande.charAt(1) == 'U' && commande.charAt(2) == 'I' && commande.charAt(3) == 'T') {
			this.commandes.write("221 Goodbye.");
			Disconnect();
		}
	}

}