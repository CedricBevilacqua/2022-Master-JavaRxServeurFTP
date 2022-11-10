package fil.com;


import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) throws IOException {
		//Lecture des arguments
		System.out.println("Démarrage du serveur...");
		String servPath;
		if(args.length > 0 && Files.exists(Paths.get(args[0]))) {
			servPath = args[0];
		} else {
			System.out.println("Merci de préciser un chemin d'accès correct au serveur en argument.");
			return;
		}
		int port = 21;
		if(args.length > 1) {
			try {
				port = Integer.parseInt(args[1]);
			} catch(Exception ex) {
				System.out.println("Merci de préciser un numéro de port correcte en argument ou aucun numéro de port pour utiliser le port 21 par défaut.");
				return;
			}
		}
		String user = "anonymous";
		if(args.length > 2) {
			user = args[2];
		}
		String password = "anonymous@example.com";
		if(args.length > 3) {
			password = args[3];
		}
		//Lancement du serveur
		ConnectManager connexionManager = new ConnectManager(port);;
		Socket connexion = null;
		FTPServer connexionFTP = null;
		
		while(true) {
			System.out.println("En attente d'une nouvelle connexion...");
			try {
				connexion = connexionManager.GetClient();
				connexionFTP = new FTPServer(connexion, servPath, user, password);
				Thread FTPCaller = new Thread(connexionFTP);
				FTPCaller.start();
			} catch (Exception e) { }
		}
	}
}