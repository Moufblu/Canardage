package canardage;

import java.net.ServerSocket;
import Protocol.ProtocolV1;
import chat.ChatMaster;
import canardage.action.Action;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import duckException.BadGameInitialisation;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Classe pour la partie d'implémentation complète du serveur de la
 * connexion client-serveur Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class ServerManager implements Runnable {

   private final Object mutex = new Object();
   
   private byte[] hash;    // Tableau pour stocker les hash des mots de passe
   private Thread thread;  // Threads pour les serveurs
   private Server server;  // Le serveur en lui-même

   private final static int NB_ACTION_CARDS = 10;  // Nombre de cartes action

   private ServerSocket serverSocket;  // Le Socket du serveur
   private Thread acceptingClients;

   private List<Action> deck;               // La pile de cartes
   private List<Action> usedCards;           //les cartes déjà jouée qu'on récupère une fois le deck vide
   private List<List<Integer>> playerCards;  // Les listes des cartes des joueurs
   private Board board;

   private int nbPlayers;  // Nombre de joueurs dans la partie

   private List<Client> playersSockets;   // Liste des Sockets des joueurs
   private final static String defaultServerName = "Canardage";
   private static String defaultHashedPassword;

   private ChatMaster chat;

   private static ServerManager instance;

   static {
      try {
         MessageDigest md = MessageDigest.getInstance(Global.Security.ENCODING_ALGORITHM);
         md.update("".getBytes(Global.Text.FORMAT_TEXT));
         defaultHashedPassword = new String(md.digest(), StandardCharsets.UTF_8);
      } catch(NoSuchAlgorithmException ex) {
         System.out.println("Couldn't hash password.");
      } catch(UnsupportedEncodingException ex) {
         System.out.println("Encoding of hash not found.");
      }
   }

   /**
    * Constructeur de la classe ServerManager
    * @param name Le nom du serveur
    * @param hash Le tableau avec les hash
    */
   private ServerManager(String name, byte[] hash) {
      deck = new ArrayList<>();
      usedCards = new ArrayList<>();
      playerCards = new ArrayList<>();
      playersSockets = new ArrayList<>();
      nbPlayers = 0;

      if(name.equals("")) {
         name = defaultServerName;
      }

      this.hash = hash;
      try {
         Socket socket = new Socket();
         socket.connect(new InetSocketAddress("google.com", 80));
         server = new Server(UUID.randomUUID(),
                 name,
                 socket.getLocalAddress().getHostAddress(),
                 ProtocolV1.PORT);
         sendInfo(socket.getLocalAddress());
         
         // TODO alternative au nombre de connexions max ou posssibilité d'interrompre la recherche des clients ?
         //Création du chat avec le nombre de joueurs max
         chat = new ChatMaster(server.getIpAddress(), Global.Rules.MAX_NB_PLAYERS);
         chat.accept();
      } catch(UnknownHostException ex) {
         System.out.println("Impossible de trouver l'adresse IP du host.");
      } catch(IOException ex) {
         System.out.println("can't create test socket to google");
      }
   }

   public static ServerManager getInstance() throws RuntimeException {
      if(instance != null) {
         return instance;
      } else {
         throw new RuntimeException("Instance not registered !");
      }
   }

   public static void registerInstance(String name, byte[] hash) {
      if(instance == null) {
         instance = new ServerManager(name, hash);
      }
   }

   /**
    * Méthode qui envoie des informations comme réponse au client
    * @param address
    */
   public final void sendInfo(final InetAddress address) {
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
                     } catch(IOException ex) {
                        System.out.println(ex + " : error sending datagram");
                     }
                  }

               }, 0, 1000);
            } catch(SocketException ex) {
               System.out.println(ex + " : n'a pas pu créer le Socket.");
            } catch(UnknownHostException ex) {
               System.out.println(ex + " : impossible de trouver l'adresse IP du host.");
            } catch(IOException ex) {
               System.out.println(ex + ": couldn't create socket");
            }
         }

      });
      thread.start();
   }

   /**
    * Initialisation du serveur
    * @throws IOException Lancée si plusieurs des essais de créer quelquechose échoue
    */
   public void acceptClients() throws IOException {
      if(serverSocket == null || serverSocket.isBound()) {
         serverSocket = new ServerSocket(ProtocolV1.PORT, Global.Rules.MAX_NB_PLAYERS);
      }

      acceptingClients = new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               do {
                  System.out.println("Attente d'une connexion au joueur");
                  final Client client = new Client(serverSocket.accept());

//                  if(nbPlayers == 0) {
//                     System.out.println("Acceptation d'une connexion au joueur");
//                     playersSockets.add(client);
//                     client.writeLine(ProtocolV1.ACCEPT_CONNECTION);
//                     nbPlayers++;
//                     continue;
//                  }
                  new Thread(new Runnable() {
                     
                     @Override
                     public void run() {

                        System.out.println("    Demande du mot de passe au joueur");
                        client.writeLine(ProtocolV1.HASH);

                        System.out.println("    Mot clé Hash reçu par le serveur");
                        try {
                           System.out.println("    Réception du Hash : " + hash.length);
                           byte[] givenHash = client.readBytes(hash.length);
                           if(Arrays.equals(givenHash, hash)) {
                              System.out.println("    Acceptation d'une connexion au joueur");
                              playersSockets.add(client);
                              client.writeLine(ProtocolV1.messageAccept(nbPlayers));
                              synchronized (mutex) {
                                 nbPlayers++;
                              }
                           } else {
                              System.out.println("    Refus de la connexion du joueur");
                              client.writeLine(ProtocolV1.REFUSE_CONNECTION);

                           }
                        } catch(IOException ex) {
                           Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }

                  }).start();

               } while(nbPlayers < Global.Rules.MAX_NB_PLAYERS);

               //serverSocket.close();
            } catch(IOException e) {
               System.out.println("Couldn't get client socket.");
            } catch(IllegalArgumentException e) {
               System.out.println("Hand of cards given is invalid.");
            }
         }
      });
      acceptingClients.start();
   }

   public void loadGame() throws BadGameInitialisation, IOException {
      if(nbPlayers < Global.Rules.MIN_NB_PLAYERS) {
         throw new BadGameInitialisation("Number of players must be at least " + Global.Rules.MIN_NB_PLAYERS);
      }

      //Killing the thread accepting clients
      if(acceptingClients.isAlive()) {
         try {
            serverSocket.close();
            acceptingClients.interrupt();
            acceptingClients.join();
         } catch(InterruptedException ex) {
            System.out.println("Thread accepting clients has been killed by admin.");
         } catch(IOException e) {
            System.out.println("Server socket couldn't be closed.");
         }
      }

      initialiseGame();
   }
   
   private void startGame()  {
      boolean gameFinished = false;
      do {
         for(Client player : playersSockets) {
            playATurn(player);
            int winner = board.won();
            if(winner > -1) {
               gameFinished = true;
               break;
            }
         }
      } while(!gameFinished);

   }
   
   private void playATurn(Client client) {
      int choiceCard = client.useCard();
      client.distribute(choiceCard, deck.remove(0));
      sendBoard();
      if(deck.size() == 0) {
         restoreDeck();
      }
      
   }
   
   private void restoreDeck() {
      while(usedCards.size() > 0) {
         deck.add(usedCards.remove(0));
      }
   }

   private void initialiseGame() {
      Board.registerInstance(nbPlayers);
      board = Board.getInstance();
      initialiseDeck();
      for(Client client : playersSockets) {
         System.out.println("Envoi d'une main");
         Action[] hand = new Action[Global.Rules.HAND_SIZE];
         for(int i = 0; i < Global.Rules.HAND_SIZE; i++) {
            hand[i] = deck.remove(0);
         }
         client.sendNewHand(hand);
      }
      sendBoard();
   }
   
   private void sendBoard() {
      for(Client client : playersSockets) {
         client.writeLine(ProtocolV1.messageBoardState());
      }
   }

   private void initialiseDeck() {
      
      for(Action card : Global.cards) {
         for(int i = 0; i < card.getNbCards(); i++) {
            try {
               deck.add(card.clone());
            } catch(CloneNotSupportedException ex) {
               Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
               throw new RuntimeException(ex.getCause());
            }
         }
      }

      Collections.shuffle(deck);
   }

   public Server getServer() {
      return server;
   }

   /**
    * Méthode qui vérifie que le serveur est en marche
    * @return Vrai si il est en marche, faux sinon
    */
   public boolean isRunning() {
      return serverSocket != null && serverSocket.isBound();
   }

   @Override
   public void run() {
      startGame();
   }
}
