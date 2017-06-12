package canardage;

import Protocol.ProtocolV1;
import canardage.action.Action;
import canardage.action.WithLocation;
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
      hand = new Action[Global.Rules.HAND_SIZE];
   }

   public void setId(int id) {
      this.id = id;
   }
   
   public int getId() {
      return id;
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
      System.arraycopy(hand, 0, this.hand, 0, hand.length);
      
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
   
   boolean hasCard(int idCard) {
      for(Action action : hand) {
         if(action.getId() == idCard) {
            return true;
         }
      }
      return false;
   }
   
   public int getLocation() throws IOException {
      writeLine(ProtocolV1.ASK_FOR_POSITION);
      int choiceLocation = Integer.parseInt(readLine());
      return choiceLocation;
   }
   
   public int useCard() {
      
      int choiceCard = -1;
      boolean hasCardWithEffect = hasAnyCardPlayable();
      
      do {
         writeLine(ProtocolV1.YOUR_TURN);
         try {
            choiceCard = Integer.parseInt(readLine());
            
            //vérifie l'intégrité du choix côté serveur
            if(!hasCard(choiceCard)) {
               throw new RuntimeException("Le joueur a joué une carte qui n'est pas dans sa main");
            }
         } catch(IOException ex) {
            //TODO
         }
      } while(!hand[choiceCard].hasEffect() || hasCardWithEffect);
      
      hand[choiceCard].effect();
      hand[choiceCard] = null;
      
      return choiceCard;
   }
   
   public void distribute(int position, Action newCard) {
      hand[position] = newCard;
      writeLine(ProtocolV1.messageDistributeCard(newCard.getId()));
   }

}
