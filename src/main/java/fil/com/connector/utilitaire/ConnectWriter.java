package fil.com.connector.utilitaire;

import java.io.IOException;

/**
 * Cette interface implémente une méthode permettant d'écrire sur les communications du serveur
 *
 * @author Cédric Bevilacqua, Sema Altinkaynak
 */
public interface ConnectWriter {
    public void write(String message) throws IOException;

}