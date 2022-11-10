package fil.com.connector;

import java.net.Socket;

/**
 * Classe abstraite reprenant le système de connexion avec une socket sur un serveur
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public abstract class Connector {
    
    protected Socket connexion;
}
