package fil.com.connector.utilitaire;

import java.io.IOException;

/**
 * Cette interface implémente une méthode permettant de lire sur les communications du serveur
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public interface ConnectReader {
    
    public String read() throws IOException;
}
