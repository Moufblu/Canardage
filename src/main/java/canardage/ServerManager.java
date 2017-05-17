package canardage;

import java.net.ServerSocket;
import Protocol.ProtocolV1;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import duckException.BadGameInitialisation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

/**
 *
 */
public class ServerManager {

   private byte[] hash;
   private Thread thread;
   private Server server;

   private final static int MIN_NB_PLAYERS = 3;
   private final static int MAX_NB_PLAYERS = 6;
   private final static int NB_ACTION_CARDS = 10;

   private ServerSocket serverSocket;
   private Thread acceptingClients;

   private List<Integer> deck;
   private List<List<Integer>> playerCards;
   private Board board;

   private int nbPlayers;

   private List<Client> playersSockets;

   public ServerManager(String name, byte[] hash) {
      deck = new ArrayList<>(NB_ACTION_CARDS);
      playerCards = new ArrayList<>();
      playersSockets = new ArrayList<>();
      nbPlayers = 0;

      this.hash = hash;
      try {
         Socket socket = new Socket();
         socket.connect(new InetSocketAddress("google.com", 80));
         server = new Server(UUID.randomUUID(), name, socket.getLocalAddress().getHostAddress(), ProtocolV1.PORT);
         sendInfo(socket.getLocalAddress());
      } catch (UnknownHostException ex) {
         System.out.println("impossible to find the ip address of the host");
      } catch (IOException ex) {
         System.out.println("can't create test socket to google");
      }
   }

   private void sendInfo(final InetAddress address) {

      thread = new Thread(new Runnable() {
         @Override
         public void run() {
            Gson gson = new Gson();
            Type type = new TypeToken<Server>() {
            }.getType();
            String msg = gson.toJson(server, type);
            try {
               byte[] payload = msg.getBytes();

               final MulticastSocket socket = new MulticastSocket(ProtocolV1.MULTICAST_PORT);
               socket.setInterface(address);
               socket.setBroadcast(true);

               final DatagramPacket datagram = new DatagramPacket(payload,
                       payload.length);
               datagram.setAddress(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
               datagram.setPort(ProtocolV1.MULTICAST_PORT);

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

               }, 0, 1000);
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

   public void acceptClients() throws IOException {
      if (serverSocket == null || serverSocket.isBound()) {
         serverSocket = new ServerSocket(ProtocolV1.PORT, MAX_NB_PLAYERS);
      }

      Thread acceptingClients = new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               for (int i = 0; i < MAX_NB_PLAYERS; i++) {
                  System.out.println("Attente d'une connexion au joueur " + i);
                  playersSockets.add(new Client(serverSocket.accept()));
                  nbPlayers++;

                  System.out.println("Acceptation d'une connexion au joueur " + i);
                  playersSockets.get(i).writeLine(ProtocolV1.ACCEPT_CONNECTION);
               }

               serverSocket.close();
            } catch (IOException e) {
               System.out.println("Couldn't get client socket.");
            } catch (IllegalArgumentException e) {
               System.out.println("Hand of cards given is invalid.");
            }
         }
      });
      acceptingClients.start();
   }

   public void startGame() throws BadGameInitialisation {
      if (nbPlayers < MIN_NB_PLAYERS) {
         throw new BadGameInitialisation("Number of players must be at least " + MIN_NB_PLAYERS);
      }

      //Killing the thread accepting clients
      if (acceptingClients.isAlive()) {
         try {
            serverSocket.close();
            acceptingClients.interrupt();
            acceptingClients.join();
         } catch (InterruptedException ex) {
            System.out.println("Thread accepting clients has been killed by admin.");
         } catch (IOException e) {
            System.out.println("Server socket couldn't be closed.");
         }
      }

      initialiseGame();

      for (Client player : playersSockets) {
         String answer = "";
         String expectedAnswer = ProtocolV1.USE_CARD + ProtocolV1.SEPARATOR + 4;

         do {
            System.out.println("Asking for card");
            player.writeLine(ProtocolV1.YOUR_TURN);

            try {
               answer = player.readLine();
            } catch (IOException ex) {
               System.out.println("Couldn't get action card.");
            }
         } while (!answer.equals(expectedAnswer));

         System.out.println("Player played the card Target");

         do {
            answer = "";
            System.out.println("Asking for position");
            player.writeLine(ProtocolV1.ASK_FOR_POSITION);

            try {
               answer = player.readLine();
            } catch (IOException ex) {
               System.out.println("Couldn't get position to play action card.");
            }
         } while (!answer.contains(ProtocolV1.ASK_FOR_POSITION));

         String[] temp = answer.split(ProtocolV1.SEPARATOR);
         int position = Integer.valueOf(temp[1]);
         System.out.println("Player played in the position " + position);

         board.setTarget(position, true);

         System.out.println("Sending updated board");
         player.writeLine(ProtocolV1.messageBoardState());
      }

   }

   private void initialiseGame() {
      Board.registerInstance(nbPlayers);
      board = Board.getInstance();
      initialiseDeck();
      for (Client client : playersSockets) {
         System.out.println("Envoi d'une main");
         int[] hand = {4, 4, 4};
         client.writeLine(ProtocolV1.messageHand(hand));
      }
   }

   private void initialiseDeck() {
      int nbCards = 10;
      for (int i = 0; i < nbCards; i++) {
         deck.add(0);
      }

      for (int i = 0; i < nbCards; i++) {
         deck.add(1);
      }

      for (int i = 0; i < nbCards; i++) {
         deck.add(2);
      }

      for (int i = 0; i < nbCards; i++) {
         deck.add(3);
      }

      for (int i = 0; i < nbCards; i++) {
         deck.add(4);
      }

      Collections.shuffle(deck);
   }

   public boolean isRunning() {
      return serverSocket != null && serverSocket.isBound();
   }
}
