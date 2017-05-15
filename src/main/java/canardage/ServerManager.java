package canardage;

import java.net.ServerSocket;
import Protocol.ProtocolV1;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Description: Classe pour la partie d'implémentation complète du serveur de la 
 * connexion client-serveur
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class ServerManager {

   private byte[] hash;    // Tableau pour stocker les hash des mots de passe
   private Thread thread;  // Threads pour les serveurs
   private Server server;  // Le serveur en lui-même

   private final static int MAX_NB_PLAYERS = 6;    // Nombre maximum de joueur
   private final static int NB_ACTION_CARDS = 10;  // Nombre de cartes action
   private ServerSocket serverSocket;              // Le Socket du serveur

   private List<Integer> deck;               // La pile de cartes
   private List<List<Integer>> playerCards;  // Les listes des cartes des joueurs

   private int nbPlayers;  // Nombre de joueurs dans la partie

   private List<Client> playersSockets;   // Liste des Sockets des joueurs

   /**
    * Constructeur de la classe ServerManager
    * @param name Le nom du serveur
    * @param hash Le tableau avec les hash
    */
   public ServerManager(String name, byte[] hash) {
      deck = new ArrayList<>(NB_ACTION_CARDS);
      playerCards = new ArrayList<>();
      playersSockets = new ArrayList<>();
      nbPlayers = 0;

      this.hash = hash;
      try {
         server = new Server(UUID.randomUUID(),
                              name,
                              InetAddress.getLocalHost().getHostAddress(),
                              ProtocolV1.PORT);
      } catch (UnknownHostException ex) {
         System.out.println("Impossible de trouver l'adresse IP du host.");
         System.out.println("impossible to find the ip address of the host");
      }
      sendInfo();
   }

   /**
    * Méthode qui envoie des informations comme réponse au client
    */
   public void sendInfo() {
      thread = new Thread(new Runnable() {
         @Override
         public void run() {
            Gson gson = new Gson();
            Type type = new TypeToken<Server>() {}.getType();
            String msg = gson.toJson(server, type);
            try {
               final DatagramSocket socket = new DatagramSocket();
               socket.setBroadcast(true);
               byte[] payload = msg.getBytes();
               final DatagramPacket datagram = new DatagramPacket(payload,
                                 payload.length,
                                 InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS),
                                                       ProtocolV1.MULTICAST_PORT);

               new Timer().scheduleAtFixedRate(new TimerTask() {

                  @Override
                  public void run() {
                     try {
                        socket.send(datagram);
                     } catch (IOException ex) {
                        System.out.println(ex + " : error sending datagram");
                     }
                  }

               }, 1000, 0);
            } catch (SocketException ex) {
               System.out.println(ex + " : n'a pas pu créer le Socket.");
               System.out.println(ex + " : couldn't create socket");
            } catch (UnknownHostException ex) {
               System.out.println(ex + " : impossible de trouver l'adresse IP du host.");
               System.out.println(ex + " : impossible to find the ip address of the host");
            }
         }
      });
   }

   /**
    * Initialisation du serveur
    * @throws IOException Lancée si plusieurs des essais de créer quelquechose échoue
    */
   public void startServer() throws IOException {
      
      if (serverSocket == null || serverSocket.isBound()) {
         serverSocket = new ServerSocket(ProtocolV1.PORT, MAX_NB_PLAYERS);
      }

      Thread serverThread = new Thread(new Runnable() {
         
         @Override
         public void run() {

            Board.registerInstance(3);
            Board board = Board.getInstance();

            if (nbPlayers < MAX_NB_PLAYERS) {
               try {
                  final int nbjoueursTest = 3;
                  for (int i = 0; i < nbjoueursTest; i++) {
                     System.out.println("Attente d'une connexion au joueur " + i);
                     playersSockets.add(new Client(serverSocket.accept()));
                     nbPlayers++;

                     System.out.println("Acceptation d'une connexion au joueur " + i);
                     playersSockets.get(i).writeLine(ProtocolV1.ACCEPT_CONNECTION);

                     System.out.println("Envoi d'une main au joueur " + i);
                     int[] hand = {1, 2, 3};
                     playersSockets.get(i).writeLine(ProtocolV1.messageHand(hand));
                  }

                  for (int i = 0; i < nbjoueursTest; i++) {

                     boolean isGood = false;
                     do {

                        System.out.println("Envoi du Board au joueur " + i);
                        playersSockets.get(i).writeLine(ProtocolV1.messageBoardState());

                        System.out.println("Demande une carte au joueur " + i);
                        playersSockets.get(i).writeLine(ProtocolV1.YOUR_TURN);

                        System.out.println("Attente d'une carte au joueur " + i);
                        String answer = playersSockets.get(i).readLine();

                        if (answer.contains(ProtocolV1.USE_CARD)) {
                           System.out.println("Message reçu : " + answer);
                           isGood = true;
                        } else {
                           playersSockets.get(i).writeLine(ProtocolV1.ERRORS[0]);
                           System.out.println("DEGUEU"); // With Naddy's voice
                           isGood = false;
                        }
                     } while (!isGood);
                  }

                  for (int i = 0; i < nbjoueursTest; i++) {

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
                           System.out.println("DEGUEU"); // With Naddy's voice
                           isGood = false;
                        }
                     } while (!isGood);
                  }

                  for (int i = 0; i < nbjoueursTest; i++) {

                     System.out.println("Envoi d'une erreur bidon"); // POUTRE (?)
                     playersSockets.get(i).writeLine(ProtocolV1.messageRefuse(1));

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
            try {
               serverSocket.close();
            } catch (IOException e) {
               System.out.println(e.getMessage());
            }
         }
      });
      serverThread.start();
   }

   /**
    * Méthode qui vérifie que le serveur est en marche
    * @return Vrai si il est en marche, faux sinon
    */
   public boolean isRunning() {
      return serverSocket != null && serverSocket.isBound();
   }
}
