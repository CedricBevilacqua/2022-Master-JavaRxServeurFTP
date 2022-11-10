package fil.com;

import java.io.IOException;
import java.net.Socket;

import fil.com.connector.CommandeConnector;

public class MockCommandeConnector extends CommandeConnector{

    String message;

    public MockCommandeConnector(Socket connexion) throws IOException {
        super(null);
    }
    

    @Override
    public void Disconnect() throws IOException {
      
    }

    @Override
    public String read() throws IOException {
        return this.message;
    }

    @Override
    public void write(String message) {
        this.message= message;
    }
    
}
