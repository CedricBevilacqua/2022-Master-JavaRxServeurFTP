package fil.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import fil.com.connector.CommandeConnector;
import fil.com.connector.DataConnector;

public class MockFTPServer {

    private CommandeConnector commandes;
	private DataConnector datas;
	private DataManager dataManager;
	private String username;
	private String password;
	
	private String fileToRename;
	private Boolean usernameAccepted;
	private Boolean passwordAccepted;
    private String message = null;
	private String liste=null;

    public MockFTPServer(Socket connexion, String servPath, String username, String password) throws Exception {
        this.commandes = new MockCommandeConnector(null);
		this.dataManager = new MockDataManager(servPath);
		this.username = username;
		this.usernameAccepted = false;
		this.password = password;
		this.passwordAccepted = false;
        
    }

    
    public void Disconnect() throws IOException {
        
    }

    public void commandes(String message){
        this.message=message;
    }

	public String liste(){
		return this.liste;
	}
    
    public void StartConnexion() throws Exception {
        if(this.usernameAccepted && this.passwordAccepted) {
            answerProtocolCommands(this.message);
        } else {
            authProtocolCommands(this.message);
        }
    }

    /**
     * message de retour dur serveur lors d'une reponse 
     * @return
     * @throws IOException
     */
    public String commandeResultat() throws IOException{
        return this.commandes.read();
    }

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
			this.liste=fileList;
			commandes.write("226 Directory send OK.");
		} else if(commande.charAt(0) == 'T' && commande.charAt(1) == 'Y' && commande.charAt(2) == 'P' && commande.charAt(3) == 'E'
				 && commande.charAt(4) == ' ' && commande.charAt(5) == 'I') {
			commandes.write("200 Switching to Binary mode.");
		} else if(commande.charAt(0) == 'E' && commande.charAt(1) == 'P' && commande.charAt(2) == 'S' && commande.charAt(3) == 'V') {
		
			commandes.write("229 Entering Extended Passive Mode (|||1234|)");
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