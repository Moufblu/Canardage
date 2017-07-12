package canardage;

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
      hand = new Action[Global.Rules.HAND_SIZE];
   }

   public void setId(int id) {
      this.id = id;
   }
   
   public int getId() {
      return id;
   }
   
   public void startSwaps() {
      writeLine(Global.ProtocolV1.START_SWAP);
   }
   
   /**
    * Méthode pour que le client puisse écrire une requête au serveur
    * @param message Le message envoyé par le client
    */
   public void writeLine(String message) {
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
      for(Action card : hand) {
         card.setPlayer(this);
      }
      System.arraycopy(hand, 0, this.hand, 0, hand.length);
      
      writeLine(canardage.Global.ProtocolV1.messageHand(Stream.of(hand)
         .map(Action::getID)
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
   
   public int getLocation() throws IOException {
      writeLine(canardage.Global.ProtocolV1.ASK_FOR_POSITION);
      String[] positionAnswer = readLine().split(canardage.Global.ProtocolV1.SEPARATOR);
      System.out.println("position recu cote server : " + positionAnswer[1]);
      int choiceLocation = Integer.parseInt(positionAnswer[1]);
      return choiceLocation;
   }
   
   public int getIDPlayer() throws IOException {
      writeLine(canardage.Global.ProtocolV1.ASK_FOR_PLAYER_ID);
      String[] positionAnswer = readLine().split(canardage.Global.ProtocolV1.SEPARATOR);
      System.out.println("idPlayer recu cote server : " + positionAnswer[1]);
      int playerIDChoice = Integer.parseInt(positionAnswer[1]);
      return playerIDChoice;
   }
   
   public void playSound(Global.SOUNDS sound) {
      writeLine(Global.ProtocolV1.messageSound(sound));
   }
   
   public CardInfo useCard() {
      
      int choiceCard = -1;
      boolean hasCardWithEffect = hasAnyCardPlayable();
      System.out.println(hasCardWithEffect);
      do {
         writeLine(canardage.Global.ProtocolV1.YOUR_TURN);
         try {
            System.out.println("ATTEND UNE REPONSE");
            String[] response = readLine().split(canardage.Global.ProtocolV1.SEPARATOR);
            choiceCard = Integer.parseInt(response[1]);
            System.out.println("carte reçu choisie : " + choiceCard);
         } catch(IOException ex) {
            ex.printStackTrace();
            System.out.println("problème lors de la reception de la carte");
         }
      } while(!hand[choiceCard].hasEffect() && hasCardWithEffect);
      System.out.println("use card no : " + hand[choiceCard]);
      boolean hasEffect = hand[choiceCard].hasEffect();
      hand[choiceCard].effect();
      CardInfo cardInfo = new CardInfo(choiceCard, hand[choiceCard].getID(), hasEffect);
      hand[choiceCard] = null;
      
      
      return cardInfo;
   }
   
   public void distribute(int position, Action newCard) {
      newCard.setPlayer(this);
      hand[position] = newCard;
      writeLine(canardage.Global.ProtocolV1.messageDistributeCard(newCard.getID()));
   }

}
