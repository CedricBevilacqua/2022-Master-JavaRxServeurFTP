package fil.com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Cette classe permet de récupérer les nouvelles connexions au serveur et d'en générer une 
 * socket associée pour chaque nouvelle connexion
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public class ConnectManager {
	private ServerSocket server;
	
	/**
	 * Initialiser l'instance qui gère la recherche de nouvelle connexion
	 *
	 * @author Cédric Bevilacqua
	 * @param port : Port sur lequel on attend les nouvelles connexions
	 * @throws Erreur de connexion
	 */
	public ConnectManager(int port) throws IOException {
		this.server = new ServerSocket(port);
	}
	
	/**
	 * Effectue un appel bloquant dans l'attente d'une nouvelle connexion puis en génère une socket
	 *
	 * @author Cédric Bevilacqua
	 * @return Socket de la connexion récupérée
	 * @throws Erreur de connexion
	 */
	public Socket GetClient() throws IOException {
		Socket newClient = server.accept();
		return newClient;
	}
}