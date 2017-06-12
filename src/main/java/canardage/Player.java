package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;
import chat.ChatClient;
import chat.Emoticon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import duckException.BadGameInitialisation;
import fxml.controller.FXMLCanardageController;
import java.io.*;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Classe pour créer et gérer les joueurs d'une partie Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Player implements Runnable {

   private FXMLCanardageController canardageFxml;
   private final long REFRESH_DELAY = 2000;
   private static final int TIMEOUT_ANSWER = 0;

   private Socket clientSocket;           // Socket du client
   private BufferedReader responseBuffer; // Buffer pour le réponse
   private PrintWriter writer;            // Writer pour les envois d'informations
   private Set<Server> servers;           // La liste des serveurs
   private OutputStream byteWriter;

   private ChatClient chatClient;

   private int playerNumber;

   private Integer[] cards;           // Liste des cartes

   boolean connected = false;             // Booléen pour connaître la connexion

   private static Player instance;
   private ServerManager server = null;

   private final static String defaultServerName = "Canardage";
   private final static String defaultPassword = "";

   /**
    * Constructeur de la classe Player
    * @param adress
    */
   private Player() {
      cards = new Integer[Global.Rules.HAND_SIZE];
      servers = new HashSet<>();
   }

   public static Player getInstance() throws RuntimeException {
      if(instance == null) {
         instance = new Player();
      }
      return instance;
   }

   public int getPlayerNumber() {
      return playerNumber;
   }

   /**
    * Méthode pour obtenir les serveurs disponibles
    * @return
    */
   public Set<Server> getServers() {
      MulticastSocket socket;
      try {

         Socket testSocket = new Socket();
         testSocket.connect(new InetSocketAddress("google.com", 80));
         InetAddress ipAddress = testSocket.getLocalAddress();
         testSocket.close();

         servers.clear();

         socket = new MulticastSocket(ProtocolV1.MULTICAST_PORT);
         socket.setInterface(ipAddress);
         socket.joinGroup(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
         socket.setSoTimeout((int) REFRESH_DELAY);

         long start = new Date().getTime();
         while(new Date().getTime() - start < REFRESH_DELAY) {
            byte[] buffer = new byte[2048];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagram);
            System.out.println("received datagram");
            String msg = new String(datagram.getData());
            msg = msg.substring(0, msg.lastIndexOf('}') + 1);
            Gson gson = new Gson();
            Type type = new TypeToken<Server>() {
            }.getType();
            if(!servers.contains((Server) gson.fromJson(msg, type))) {
               servers.add((Server) gson.fromJson(msg, type));
            }
         }
         socket.leaveGroup(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
      } catch(SocketException ex) {
         System.out.println("socket creation fail : " + ex.getMessage());
      } catch(IOException ex) {
         System.out.println("read broadcast fail : " + ex.getMessage());
      }
      return servers;
   }

   /**
    * Méthode de hachage des mots de passe
    * @param password Mot de passe à faire le hachage
    * @return Un tableau de byte avec le hash
    * @throws NoSuchAlgorithmException Erreur en faisant le hash
    * @throws UnsupportedEncodingException Erreur avec l'encodage
    */
   private byte[] hash(String password) throws NoSuchAlgorithmException,
           UnsupportedEncodingException {
      MessageDigest md = MessageDigest.getInstance(Global.Security.ENCODING_ALGORITHM);
      md.update(password.getBytes(Global.Text.FORMAT_TEXT));
      return md.digest();
   }

   /**
    * Méthode pour la création du serveur
    * @param name Le nom du serveur si donné
    * @param password Le mot de passe pour la partie si donné
    * @return le serveur créé
    */
   public boolean createServer(String name, String password) {
      byte[] hash;
      boolean serverCreated = false;
      try {
         hash = hash(password);
      } catch(UnsupportedEncodingException | NoSuchAlgorithmException ex) {
         throw new RuntimeException(ex.getCause());
      }
      ServerManager.registerInstance(name, hash);
      server = ServerManager.getInstance();
      if(!server.isRunning()) {
         try {
            server.acceptClients();
            connect(server.getServer(), password);
            serverCreated = true;
         } catch(IOException e) {
            System.out.println(e.getMessage());
         } catch(NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getCause());
         }
      }

      return serverCreated;
   }

   public void showServers() {
      for(Server s : servers) {
         System.out.println(s.toString());
      }
   }

   @Override
   public void run() {
      String inputServer;
      String[] splittedCommand = {""};
      int locationChoice = 0;

      do {
         try {
            inputServer = responseBuffer.readLine();
            splittedCommand = inputServer.split(ProtocolV1.SEPARATOR);
         } catch(IOException e) {
            System.out.println(e.toString());
            continue;
         }

         switch(splittedCommand[0]) {
            case ProtocolV1.ASK_FOR_POSITION:
               canardageFxml.askPosition();
               break;
            case ProtocolV1.DISTRIBUTE_CARD:
               if(cards.length > 0) {
                  throw new IllegalStateException("Main du joueurs pleine");
               } else {
                  cards[locationChoice] = readLineCardFileInfo(Integer.parseInt(splittedCommand[1]));
                  canardageFxml.updateCards(cards);
               }
               break;
            case ProtocolV1.DISTRIBUTE_HAND:
               if(cards.length > 0) {
                  throw new IllegalStateException("Action cards already distributed");
               } else {
                  for(int i = 1; i < Global.Rules.HAND_SIZE + 1; i++) {
                     cards[i] = readLineCardFileInfo(Integer.parseInt(splittedCommand[i]));
                  }
                  canardageFxml.updateCards(cards);
               }
               break;
            case ProtocolV1.PATCH_BOARD:
               canardageFxml.updateBoard(splittedCommand[1]);
               break;
            case ProtocolV1.YOUR_TURN:
               canardageFxml.askCard();
               break;
            case ProtocolV1.REFUSE_CARD:
               canardageFxml.alert(Global.ERROR_MESSAGES.valueOf(splittedCommand[1]));
               System.out.println(ProtocolV1.ERRORS[Integer.parseInt(splittedCommand[1])]);
               break;
         }
      } while(!splittedCommand[0].equals(ProtocolV1.END_GAME));
   }

   public void playCard(int posCard) {
      writer.println(ProtocolV1.messageUseCard(posCard));
      writer.flush();
   }
   
   public void posChoose(int position) {
      writer.println(ProtocolV1.messageAskPosition(position));
      writer.flush();
   }
   
   public void startGame() {
      server.run();
   }
   
   /**
    * Intialisation de la partie
    * @param canardageFxml
    * @throws IllegalStateException Lancée si trop de cartes dans la main du joueur,
    * @throws BadGameInitialisation Lancée si le nombre de joueurs est trop faible
    * si pas de cartes pas de connexion avec le serveur
    */
   public void startGame(FXMLCanardageController canardageFxml) throws IllegalStateException, BadGameInitialisation, IOException {
      if(isConnected()) {
         
         this.canardageFxml = canardageFxml;
         Thread thread = new Thread(this);
         thread.start();
      } else {
         throw new IllegalStateException("Vous devez vous connecter à un serveur "
                 + "avant de pouvoir commencer une partie.");
      }
   }

   /**
    * Affichage du plateau de jeu
    * @param toShow Tableau de String avec ce qu'il faut afficher
    */
   public void showBoard(String[] toShow) {
      System.out.println("Board : ");
      for(int i = 1; i < toShow.length; i++) {
         System.out.print(toShow[i] + " ");
      }
      System.out.println("");
   }

   /**
    * Méthode pour réaliser la connexion
    * @param server
    * @param mdp
    * @return
    * @throws IOException Erreur si on refuse la connexion
    * @throws java.security.NoSuchAlgorithmException
    */
   public boolean connect(Server server, String mdp) throws IOException, NoSuchAlgorithmException {
      if(!connected) {

         System.out.println("tentative de connection");
         clientSocket = new Socket(server.getIpAddress(), ProtocolV1.PORT);
         clientSocket.setSoTimeout(TIMEOUT_ANSWER);

         responseBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
         byteWriter = clientSocket.getOutputStream();
         writer = new PrintWriter(new OutputStreamWriter(byteWriter, "UTF-8"));

         System.out.println("Attente du serveur");
         String[] answer = (responseBuffer.readLine()).split(ProtocolV1.SEPARATOR);
         System.out.println(answer[0]);
         if(answer[0].equals(ProtocolV1.HASH)) {
            byte[] hashedPassword = hash(mdp);
            System.out.println("Envoi du mot de passe Hashé : " + hashedPassword.length);
            byteWriter.write(hashedPassword);
            byteWriter.flush();
         } else {
            System.out.println("Probleme Hash");
            throw new ProtocolException("erreur dans le protocole");
         }
         System.out.println("Phase d'acceptation");
         answer = (responseBuffer.readLine()).split(ProtocolV1.SEPARATOR);
         System.out.println(answer[0]);
         if(answer[0].equals(ProtocolV1.ACCEPT_CONNECTION)) {
            System.out.println("Joueur connecté");
            connected = true;
            playerNumber = Integer.valueOf(answer[1]);
            /* Dés que la partie commence on crée un thread qui écoute les messages
             * envoyés par le chat.
             */
            System.out.println("Information chat : " + clientSocket.getInetAddress().getHostAddress() + " - " + playerNumber);
            chatClient = new ChatClient(clientSocket.getInetAddress().getHostAddress(), playerNumber); // erreur ici
            chatClient.listen();
         } else if(!answer[0].equals(ProtocolV1.REFUSE_CONNECTION)) {
            throw new ProtocolException("erreur dans le protocole");
         }
      }

      System.out.println("resultat de la tentative de connexion : " + connected);
      return connected;
   }

   /**
    * Méthode pour vérifier si on est connecté
    * @return Vrai si on est connecté, faux sinon
    */
   public boolean isConnected() {
      return connected;
   }

   /**
    * Obtention de la carte choisie par le joueur pour être utilisée
    * @return Le numéro de la carte à utiliser
    */
   public int getCardChoice() {
      Scanner in = new Scanner(System.in);
      int cardChoice = 0;

      // Boucle tant que le joueur ne choisi pas le bon numéro d'une carte
      while(true) {
         try {
            System.out.println("Veuillez choisir une carte : (1..3)");
            for(Integer i : cards) {
               readLineCardFileInfo(i);
            }
            int positionCard = in.nextInt();
            if(positionCard <= 0 || positionCard > Global.Rules.HAND_SIZE) {
               continue;
            }
            cardChoice = cards[positionCard - 1];
            break;
         } catch(InputMismatchException e) {
            System.out.println("Entrée non valide.");
         }
      }
      return cardChoice;
   }

   /**
    * Méthode pour la lecture de la carte selectionnée
    * @param lineNo Le numéro de la carte
    * @return L'information sur la carte
    * @throws IllegalArgumentException Lancée si le numéro de la carte est erroné
    */
   public int readLineCardFileInfo(int lineNo) throws IllegalArgumentException {
      try {
         BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream("cards.txt")));
         String line;
         int counter = 0;
         while((line = buff.readLine()) != null) {
            if(counter == lineNo) {
               String[] infosCard = line.split(";");
               System.out.println(infosCard[1]);
               return Integer.parseInt(infosCard[0]);
            }
            counter++;
         }
         buff.close();
      } catch(IOException | NumberFormatException e) {
         System.out.println(e.toString());
      }
      throw new IllegalArgumentException("Numéro de carte invalide.");
   }

   /**
    * Méthode qui nous donne le choix de position donnée par un joueur
    * @return La position choisie
    */
   public int getLocationChoice() {
      Scanner in = new Scanner(System.in);
      int positionChoice;

      while(true) {
         try {
            System.out.println("Veuillez entrer une position valide : (0..5)");
            positionChoice = in.nextInt();
            break;
         } catch(InputMismatchException e) {
            System.out.println("Entrée non valide");
         }
      }
      return positionChoice;
   }

   public void sendEmoticon(Emoticon emoticon) {
      chatClient.write(emoticon);
   }

   /*public void createServer() {
    boolean nameNotRedondant = false;
    while (!nameNotRedondant) {
    nameNotRedondant = true;
    player.getServers();
    System.out.println("quel est le nom du serveur ?");
    in.reset();
    answerNameServer = in.nextLine();
    answerNameServer = answerNameServer.equals("") ? defaultServerName : answerNameServer;

    for (Server server : player.servers) {
    if (server.getName().equals(answerNameServer)) {
    nameNotRedondant = false;
    }
    }
    }
    System.out.println("quel est le mot de passe ?");
    in.reset();
    String answerPassword = in.nextLine();
    answerPassword = answerPassword.equals("") ? defaultPassword : answerPassword;
    System.out.println("NOM : " + answerNameServer + ", MDP : " + answerPassword);
    ServerManager server = player.createServer(answerNameServer, answerPassword);
    do {
    System.out.println("'go' pour commencer!!!");
    answer = in.next();
    if(answer.equals("go")) {
    try {
    server.startGame();
    break;
    } catch(BadGameInitialisation e) {
    System.out.println(e.getMessage());
    }
    }
    } while(true);
    }*/
//   public static void main(String... args) {
//      Player player = new Player(args[0]);
//      player.getServer();
//   }
   public static void main(String... args) {
//      Player player = new Player();
//      System.out.println("souhaitez-vous creer ou rejoindre un server ? (c/r)");
//      Scanner in = new Scanner(System.in);
//      String answer;
//      String answerNameServer = defaultServerName;
//      boolean nameNotRedondant = false;
//      while(!nameNotRedondant) {
//         nameNotRedondant = true;
//         player.getServers();
//         System.out.println("quel est le nom du serveur ?");
//         in.reset();
//         answerNameServer = in.nextLine();
//         answerNameServer = answerNameServer.equals("") ? defaultServerName : answerNameServer;
//
//         for(Server server : player.servers) {
//            if(server.getName().equals(answerNameServer)) {
//               nameNotRedondant = false;
//            }
//         }
//      }
//      System.out.println("quel est le mot de passe ?");
//      in.reset();
//      String answerPassword = in.nextLine();
//      answerPassword = answerPassword.equals("") ? defaultPassword : answerPassword;
//      System.out.println("NOM : " + answerNameServer + ", MDP : " + answerPassword);
//      ServerManager server = player.createServer(answerNameServer, answerPassword);
//      do {
//         System.out.println("'go' pour commencer!!!");
//         answer = in.next();
//         if(answer.equals("go")) {
//            try {
//               server.startGame();
//               break;
//            } catch(BadGameInitialisation e) {
//               System.out.println(e.getMessage());
//            }
//         }
//      } while(true);
//
//      player.startGame();
   }

}
