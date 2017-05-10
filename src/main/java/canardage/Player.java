package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;
import java.io.*;
import java.util.*;

/**
 *
 */
public class Player {
   
   private final int HAND_CARDS_NUMBER = 3;
   
   private Socket clientSocket;
   private BufferedReader responseBuffer;
   private PrintWriter writer;

   private List<Integer> cards;

   boolean connected = false;

   /**
    * 
    * @param adress 
    */
   public Player(String adress) {
      cards = new ArrayList<Integer>();
      
      try {
         connect(adress);
      } catch (IOException e) {
         System.out.println(e.toString());
      }
   }

   /**
    * 
    * @throws IllegalStateException 
    */
   private void startGame() throws IllegalStateException {
      if (isConnected()) {
         
         String inputServer;
         String[] splittedCommand = {""};
         
         do {
            try {
               inputServer = responseBuffer.readLine();
               splittedCommand = inputServer.split(ProtocolV1.SEPARATOR);
            } catch(IOException e) {
               System.out.println(e.toString());
               continue;
            }
            
            switch (splittedCommand[0]){
               case ProtocolV1.ASK_FOR_POSITION:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  writer.flush();
                  break;
               case ProtocolV1.DISTRIBUTE_CARD:
                  if(cards.size() > 0) {
                     throw new IllegalStateException("Action Cards of player full yet");
                  } else {
                     
                     cards.add(readLineCardFileInfo(Integer.parseInt(splittedCommand[1])));
                  }
                  break;
               case ProtocolV1.DISTRIBUTE_HAND:
                  if(cards.size() > 0) {
                     throw new IllegalStateException("Action cards yet distributed");
                  } else {
                     for(int i = 1; i < ProtocolV1.HAND_SIZE + 1; i++) {
                        cards.add(readLineCardFileInfo(Integer.parseInt(splittedCommand[i])));
                     }
                  }
                  break;
               case ProtocolV1.PATCH_BOARD:
                  showBoard(splittedCommand);
                  break;
               case ProtocolV1.YOUR_TURN:
                  writer.println(ProtocolV1.messageUseCard(getCardChoice()));
                  writer.flush();
                  break;
               case ProtocolV1.REFUSE_CARD:
                  System.out.println(ProtocolV1.ERRORS[Integer.parseInt(splittedCommand[1])]);
                  break;
            }
         }while(!splittedCommand[0].equals(ProtocolV1.END_GAME));
      } else {
         throw new IllegalStateException("vous devez vous connecter à un serveur avant de pouvoir commencer une partie");
      }
   }
   
   /**
    * 
    * @param toShow 
    */
   public void showBoard(String[] toShow) {
      for(int i = 1; i < toShow.length; i++) {
         System.out.print(toShow[i] + " ");
      }
   }
   
   /**
    * 
    * @param adress
    * @throws IOException 
    */
   public void connect(String adress) throws IOException {
      if (!isConnected()) {
         clientSocket = new Socket(adress, ProtocolV1.PORT);

         responseBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
         writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

         //We read the first answer from the server
         String answer = responseBuffer.readLine();

         if (answer.equals(ProtocolV1.ACCEPT_CONNECTION)) {
            connected = true;
         } else if (answer.equals(ProtocolV1.REFUSE_CONNECTION)) {
            System.out.println("Connection Refusee");
         } else {
            System.out.println("reponse reçue: " + answer);
         }
      }
   }

   /**
    * 
    * @return 
    */
   public boolean isConnected() {
      return connected;
   }
   
   /**
    * 
    * @return 
    */
   public int getCardChoice() {
      Scanner in = new Scanner(System.in);
      int cardChoice = 0;

      // Loop while the user choose a bad move
      while (true) {
         try {
            System.out.println("Veuillez choisir une carte : (1..3)");
            for(Integer i : cards) {
               readLineCardFileInfo(i);
            }
            int positionCard = in.nextInt();
            if(positionCard <= 0 || positionCard > ProtocolV1.HAND_SIZE) {
               continue;
            }
            cardChoice = cards.get(positionCard - 1);
            break;
         } catch (InputMismatchException e) {
            System.out.println("entrée non valide");
         }
      }
      return cardChoice;
   }
   
   /**
    * 
    * @param lineNo
    * @return
    * @throws IllegalArgumentException 
    */
   public int readLineCardFileInfo(int lineNo) throws IllegalArgumentException {
      try{
         BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream("cards.txt")));
         String line;
         int counter = 0;
         while ((line = buff.readLine())!=null){
            if(counter == lineNo) {
               String[] infosCard = line.split(";");
               System.out.println(infosCard[1]);
               return Integer.parseInt(infosCard[0]);
            }
            counter++;
         }
         buff.close(); 
      } catch (Exception e){
         System.out.println(e.toString());
      }
      throw new IllegalArgumentException("card number not valid");
   }
   
   /**
    * 
    * @return 
    */
   public int getLocationChoice() {
      Scanner in = new Scanner(System.in);
      int positionChoice;

      while (true) {
         try {
            System.out.println("Veuillez entrer une position valide : (0..5)");
            positionChoice = in.nextInt();
            break;
         } catch (InputMismatchException e) {
            System.out.println("entrée non valide");
         }
      }
      return positionChoice;
   }
   
   public static void main(String... args) {
      Player player = new Player(args[0]);
      player.startGame();
   }
}
