package canardage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Description: Classe pour la partie client de la connexion client-serveur Date:
 * 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Client {

   private Socket clientSocket;
   private BufferedReader reader;
   private InputStream byteReader;
   private PrintWriter writer;

   /**
    * Constructeur de la classe Client
    * @param clientSocket Socket du client
    * @throws IOException Lance une exception si on arrive pas à créer le Buffer ou
    * le Writer pour le client
    */
   public Client(Socket clientSocket) throws IOException {
      this.clientSocket = clientSocket;
      byteReader = clientSocket.getInputStream();
      reader = new BufferedReader(new InputStreamReader(byteReader));
      writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
   }

   /**
    * Méthode pour que le client puisse écrire une requête au serveur
    * @param message Le message envoyé par le client
    */
   void writeLine(String message) {
      writer.println(message);
      writer.flush();
   }

   byte[] readBytes() throws IOException{
      byte[] message = null;
      
      byteReader.read(message);
      
      return message;
   }
   
   /**
    * Lecture du client de la réponse donnée par le serveur
    * @return La réponse donnée par le serveur
    * @throws IOException Exception si on a pas réussi à lire la réponse du serveur
    */
   String readLine() throws IOException {
      return reader.readLine();
   }

   /**
    * Fermeture du socket du client
    * @throws IOException Erreur si on a pas réussi à fermer la connexion
    */
   void close() throws IOException {
      clientSocket.close();
   }

}
