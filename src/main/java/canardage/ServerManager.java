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
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ServerManager {

   private byte[] hash;
   private Thread thread;
   private Server server;

   private final static int MAX_NB_PLAYERS = 6;
   private final static int NB_ACTION_CARDS = 10;
   private ServerSocket serverSocket;

   private List<Integer> deck;
   private List<List<Integer>> playerCards;

   private int nbPlayers;

   private List<Client> playersSockets;

   public ServerManager(String name, byte[] hash) {
      deck = new ArrayList<>(NB_ACTION_CARDS);
      playerCards = new ArrayList<>();
      playersSockets = new ArrayList<>();
      nbPlayers = 0;

      this.hash = hash;
      try {
         server = new Server(UUID.randomUUID(), name, InetAddress.getLocalHost().getHostAddress(), ProtocolV1.PORT);
      } catch (UnknownHostException ex) {
         System.out.println("impossible to find the ip address of the host");
      }
      sendInfo();
   }

   public void sendInfo() {
      thread = new Thread(new Runnable() {
         @Override
         public void run() {
            Gson gson = new Gson();
            Type type = new TypeToken<Server>() {}.getType();
            String msg = gson.toJson(server, type);
            try {
               final MulticastSocket socket = new MulticastSocket();
               socket.joinGroup(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
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
                        System.out.println("sending longueur :" + datagram.getLength() + " port: " + datagram.getPort() + " data: " + datagram.getData());
                     } catch (IOException ex) {
                        System.out.println(ex + " : error sending datagram");
                     }
                  }

               }, 1, 1000);
            } catch (SocketException ex) {
               System.out.println(ex + " : couldn't create socket");
            } catch (UnknownHostException ex) {
               System.out.println(ex + " : impossible to find the ip address of the host");
            } catch (IOException ex) {
               System.out.println(ex + ": couldn't create socket");
            }
         }

      });
      thread.start();
   }

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
                           System.out.println("DEGUEU");
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
                           System.out.println("DEGUEU");
                           isGood = false;
                        }
                     } while (!isGood);
                  }

                  for (int i = 0; i < nbjoueursTest; i++) {

                     System.out.println("Envoi d'une erreur bidon");
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

   public boolean isRunning() {
      return serverSocket != null && serverSocket.isBound();
   }
}
