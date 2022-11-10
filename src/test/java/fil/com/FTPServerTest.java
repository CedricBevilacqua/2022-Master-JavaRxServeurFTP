package fil.com;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FTPServerTest {
    

    @Test
    public void testTypeChiffreme() throws Exception {
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("AUTH TLS");
        server.StartConnexion();
        assertEquals("530 Please login with USER and PASS.", server.commandeResultat());
        server.commandes("AUTH SSL");
        server.StartConnexion();
        assertEquals("530 Please login with USER and PASS.", server.commandeResultat());
        
        
    }

    @Test
    public void testTypeChiffrement() throws Exception {
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("AUTH TLS");
        server.StartConnexion();
        assertEquals("530 Please login with USER and PASS.", server.commandeResultat());
        server.commandes("AUTH SSL");
        server.StartConnexion();
        assertEquals("530 Please login with USER and PASS.", server.commandeResultat());
        server.Disconnect();
    }

    @Test 
    public void testAuthentification() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        assertEquals("331 Please specify the password.", server.commandeResultat());
        server.commandes("PASS password");
        server.StartConnexion();
        assertEquals("230 Login successful.", server.commandeResultat());
    }

    @Test (expected = Exception.class)
    public void testAuthentificationThrownError() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("USER ");
        server.StartConnexion();
    }

    @Test (expected = Exception.class)
    public void testAuthentificationPasswordThrownError() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS ");
        server.StartConnexion();
    }

    @Test
    public void testCommandeSystem() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("AUTH TLS");
        server.StartConnexion();
        server.commandes("AUTH SSL");
        server.StartConnexion();
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        assertEquals("215 UNIX Type: L8", server.commandeResultat());
    }

    @Test
    public void testCommandeFeat() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        assertEquals("211 End", server.commandeResultat());
    }
    @Test
    public void testCurrentDirectoryCommandePWD() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        server.commandes("PWD");
        server.StartConnexion();
        assertEquals("257 \"/\" is the current directory", server.commandeResultat());
    }

    @Test
    public void testTypeBinaryMode() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        server.commandes("PWD");
        server.StartConnexion();
        server.commandes("TYPE I");
        server.StartConnexion();
        assertEquals("200 Switching to Binary mode.", server.commandeResultat());
    }

    @Test
    public void testEPSV() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        server.commandes("PWD");
        server.StartConnexion();
        server.commandes("TYPE I");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        assertEquals("229 Entering Extended Passive Mode (|||1234|)", server.commandeResultat());
    }

    @Test
    public void testList() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil/", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        server.commandes("CWD /projet-compil/");
        server.StartConnexion();
        server.commandes("PWD");
        server.StartConnexion();
        server.commandes("TYPE I");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        server.commandes("LIST");
        server.StartConnexion();
        assertEquals("-rw-r--r--  194 26 jan 00:00 admin.sh\ndrwxr-xr-x  1384 12 fév 15:26 isi-tp1-droits\n", server.liste());
        assertEquals("226 Directory send OK.", server.commandeResultat());
    }

    @Test
    public void testCommandeCWDRepertoireEnfant() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil/", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        server.commandes("CWD /projet-compil/isi-tp1-droits");
        server.StartConnexion();
        server.commandes("PWD");
        server.StartConnexion();
        server.commandes("TYPE I");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        server.commandes("LIST");
        server.StartConnexion();
        assertEquals("-rw-r--r--  1794 13 fév 17:33 ex.c\n", server.liste());
        assertEquals("226 Directory send OK.", server.commandeResultat());
    }
    
    @Test
    public void testCommandeCreationRepertoire() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil/", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        server.commandes("CWD /projet-compil/isi-tp1-droits");
        server.StartConnexion();
        server.commandes("PWD");
        server.StartConnexion();
        server.commandes("TYPE I");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        server.commandes("LIST");
        server.StartConnexion();
        server.commandes("MKD nouveau");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        server.commandes("LIST");
        server.StartConnexion();
        assertEquals("-rw-r--r--  1794 13 fév 17:33 ex.c\ndrwxr-xr-x  1384 12 fév 15:26 nouveau\n", server.liste());
        assertEquals("226 Directory send OK.", server.commandeResultat());
        
    }

    @Test
    public void testCommandeSupressionRepertoire() throws Exception{
        MockFTPServer server = new MockFTPServer(null, "/projet-compil/", "anonymous", "password");
        server.commandes("USER anonymous");
        server.StartConnexion();
        server.commandes("PASS password");
        server.StartConnexion();
        server.commandes("SYST");
        server.StartConnexion();
        server.commandes("FEAT");
        server.StartConnexion();
        server.commandes("CWD /projet-compil/isi-tp1-droits");
        server.StartConnexion();
        server.commandes("PWD");
        server.StartConnexion();
        server.commandes("TYPE I");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        server.commandes("LIST");
        server.StartConnexion();
        server.commandes("MKD nouveau");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        server.commandes("LIST");
        server.StartConnexion();
        assertEquals("-rw-r--r--  1794 13 fév 17:33 ex.c\ndrwxr-xr-x  1384 12 fév 15:26 nouveau\n", server.liste());
        assertEquals("226 Directory send OK.", server.commandeResultat());
        server.commandes("RMD nouveau");
        server.StartConnexion();
        server.commandes("EPSV");
        server.StartConnexion();
        server.commandes("LIST");
        server.StartConnexion();
        assertEquals("-rw-r--r--  1794 13 fév 17:33 ex.c\n", server.liste());
        assertEquals("226 Directory send OK.", server.commandeResultat());

        
    }

}
