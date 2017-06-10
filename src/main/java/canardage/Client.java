package canardage;

import Protocol.ProtocolV1;
import canardage.action.Action;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.Stream;

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
   private Action[] hand;
   private int id;

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
      hand = new Action[ProtocolV1.HAND_SIZE];
   }

   public void setId(int id) {
      this.id = id;
   }
   
   /**
    * Méthode pour que le client puisse écrire une requête au serveur
    * @param message Le message envoyé par le client
    */
   void writeLine(String message) {
      writer.println(message);
      writer.flush();
   }

   byte[] readBytes(int size) throws IOException{
      byte[] message = new byte[size];
      
      byteReader.read(message, 0, size);
      
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
   
   public void sendNewHand(Action[] hand) {
      writeLine(ProtocolV1.messageHand(Stream.of(hand)
         .map(Action::getId)
         .toArray(Integer[]::new)
      ));
   }

   /**
    * Fermeture du socket du client
    * @throws IOException Erreur si on a pas réussi à fermer la connexion
    */
   void close() throws IOException {
      clientSocket.close();
   }

   boolean hasAnyCardPlayable() {
      for(Action cardId : hand) {
         if(cardId.hasEffect()) {
            return true;
         }
      }
      return false;
   }

}
