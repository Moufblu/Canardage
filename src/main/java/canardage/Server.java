package canardage;

import java.net.ServerSocket;
import Protocol.ProtocolV1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Server {

   private final static int MAX_NB_PLAYERS = 6;
   private final static int NB_ACTION_CARDS = 10;
   private ServerSocket serverSocket;

   private List<Integer> deck;
   private List<List<Integer>> playerCards;

   private int nbPlayers;

   private List<Client> playersSockets;

   public Server() {
      deck = new ArrayList<>(NB_ACTION_CARDS);
      playerCards = new ArrayList<>();
      playersSockets = new ArrayList<>();
      nbPlayers = 0;
   }

   public void startServer() throws IOException {
      if (serverSocket == null || serverSocket.isBound()) {
         serverSocket = new ServerSocket(ProtocolV1.PORT, MAX_NB_PLAYERS);
      }

      Thread serverThread = new Thread(new Runnable() {
         @Override
         public void run() {
            if (nbPlayers < MAX_NB_PLAYERS) {
               try {

                  for (int i = 0; i < 2; i++) {
                     System.out.println("Attente d'une connexion au joueur " + i);
                     playersSockets.add(new Client(serverSocket.accept()));
                     nbPlayers++;

                     System.out.println("Acceptation d'une connexion au joueur " + i);
                     playersSockets.get(i).writeLine(ProtocolV1.ACCEPT_CONNECTION);

                     System.out.println("Envoi d'une main au joueur " + i);
                     int[] hand = {1, 2, 3};
                     playersSockets.get(i).writeLine(ProtocolV1.messageHand(hand));
                  }

                  for (int i = 0; i < 2; i++) {

                     boolean isGood = false;
                     do {
                        System.out.println("Demande une position au joueur " + i);
                        playersSockets.get(i).writeLine(ProtocolV1.ASK_FOR_POSITION);

                        System.out.println("Attente d'une position au joueur " + i);
                        String answer = playersSockets.get(i).readLine();

                        if (answer.contains(ProtocolV1.ASK_FOR_POSITION)) {
                           System.out.println("Message reçu : " + answer);
                           isGood = true;
                        } else {
                           playersSockets.get(i).writeLine(ProtocolV1.ERRORS[0]);
                           System.out.println("DEGUEU");
                           isGood = false;
                        }
                     } while (!isGood);
                  }
                  
                  for (int i = 0; i < 2; i++) {

                     boolean isGood = false;
                     do {
                        System.out.println("Demande une carte au joueur " + i);
                        playersSockets.get(i).writeLine(ProtocolV1.YOUR_TURN);

                        System.out.println("Attente d'une carte au joueur " + i);
                        String answer = playersSockets.get(i).readLine();

                        if (answer.contains(ProtocolV1.USE_CARD)) {
                           System.out.println("Message reçu : " + answer);
                           isGood = true;
                        } else {
                           playersSockets.get(i).writeLine(ProtocolV1.ERRORS[0]);
                           System.out.println("DEGUEU");
                           isGood = false;
                        }
                     } while (!isGood);
                  }

                  for (int i = 0; i < 2; i++) {
                     System.out.println("Annonce la fin de partie au joueur " + i);
                     playersSockets.get(i).writeLine(ProtocolV1.END_GAME);

                     System.out.println("Ferme le client du joueur " + i);
                     playersSockets.get(i).close();
                  }

               } catch (IOException e) {
                  System.out.println(e.getMessage());
               }
            }

            //Uniquement pour itération 3
            if (nbPlayers == 2) {
               try {
                  serverSocket.close();

               } catch (IOException e) {
                  System.out.println(e.getMessage());
               }
            }
         }
      });
      serverThread.start();
   }

   public boolean isRunning() {
      return serverSocket != null && serverSocket.isBound();
   }

   public static void main(String... args) {
      Server server = new Server();
      if (!server.isRunning()) {
         try {
            server.startServer();
         } catch (IOException e) {
            System.out.println(e.getMessage());
         }
      }
   }
}
