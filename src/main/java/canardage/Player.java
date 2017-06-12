package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;
import chat.ChatClient;
import chat.Emoticon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import duckException.BadGameInitialisation;
import java.io.*;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Classe pour créer et gérer les joueurs d'une partie
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Player {
   private final int HAND_CARDS_NUMBER = 3;  // Nombre de cartes maximal d'un joueur
   private final long REFRESH_DELAY = 2000;
   private static final int TIMEOUT_ANSWER = 0;

   private final String ENCODING_ALGORITHM = "SHA-256";  // Algorithme de hachage
   private final String FORMAT_TEXT = "UTF-8";  // Format d'encodage du texte
   
   private Socket clientSocket;           // Socket du client
   private BufferedReader responseBuffer; // Buffer pour le réponse
   private PrintWriter writer;            // Writer pour les envois d'informations
   private Set<Server> servers;           // La liste des serveurs
   private OutputStream byteWriter;
   
   private ChatClient chatClient;
   
   private int playerNumber;

   private List<Integer> cards;           // Liste des cartes

   boolean connected = false;             // Booléen pour connaître la connexion
   
   private final static String defaultServerName = "Canardage";
   private final static String defaultPassword = "";

   /**
    * Constructeur de la classe Player
    * @param adress
    */
   public Player(String adress) {
      cards = new ArrayList<>();
      servers = new HashSet<>();
   }

   /**
    * Méthode pour obtenir les serveurs disponibles
    */
   public void getServers() {
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
         socket.setSoTimeout((int)REFRESH_DELAY);
         
         long start = new Date().getTime();
         while (new Date().getTime() - start < REFRESH_DELAY) {
            byte[] buffer = new byte[2048];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagram);
            System.out.println("received datagram");
            String msg = new String(datagram.getData());
            msg = msg.substring(0, msg.lastIndexOf('}') + 1);
            Gson gson = new Gson();
            Type type = new TypeToken<Server>() {}.getType();
            if(!servers.contains((Server)gson.fromJson(msg, type))) {
               servers.add((Server)gson.fromJson(msg, type));
            }
         }
         socket.leaveGroup(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
      } catch (SocketException ex) {
         System.out.println("socket creation fail : " + ex.getMessage());
      } catch (IOException ex) {
         System.out.println("read broadcast fail : " + ex.getMessage());
      }
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
      MessageDigest md = MessageDigest.getInstance(ENCODING_ALGORITHM);
      md.update(password.getBytes(FORMAT_TEXT));
      return md.digest();
   }
   
   /**
    * Méthode pour la création du serveur
    * @param name Le nom du serveur si donné
    * @param password Le mot de passe pour la partie si donné
    */
   public ServerManager createServer(String name, String password) {
      byte[] hash;

      try {
         hash = hash(password);
      } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
         throw new RuntimeException(ex.getCause());
      }

      ServerManager server = new ServerManager(name, hash);
      if(!server.isRunning()) {
         try {
            server.acceptClients();
            connect(server.getServer());
         } catch (IOException e) {
            System.out.println(e.getMessage());
         } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getCause());
         }
      }
      
      return server;
   }

   public void showServers() {
      for (Server s : servers) {
         System.out.println(s.toString());
      }
   }

   /**
    * Intialisation de la partie
    * @throws IllegalStateException Lancée si trop de cartes dans la main du joueur, 
    * si pas de cartes pas de connexion avec le serveur
    */
   private void startGame() throws IllegalStateException {
      if (isConnected()) {
         
         String inputServer;
         String[] splittedCommand = {""};

         do {
            try {
               inputServer = responseBuffer.readLine();
               splittedCommand = inputServer.split(ProtocolV1.SEPARATOR);
            } catch (IOException e) {
               System.out.println(e.getMessage());
               continue;
            }

            switch (splittedCommand[0]) {
               case ProtocolV1.ASK_FOR_POSITION:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  writer.flush();
                  break;
               case ProtocolV1.DISTRIBUTE_CARD:
                  if(cards.size() > 0) {
                     throw new IllegalStateException("Main du joueurs pleine");
                  } else {

                     cards.add(readLineCardFileInfo(Integer.parseInt(splittedCommand[1])));
                  }
                  break;
               case ProtocolV1.DISTRIBUTE_HAND:
                  if(cards.size() > 0) {
                     // Aucun sense dans le message d'erreur, comprend pas :/
                     // Vérification inversée? Jimmy shit? Am I a shit?
                     // Beep beep I'm a sheep, Beepbeep I'm a sheep (8)
                     throw new IllegalStateException("Action cards yet distributed");
                  } else {
                     for (int i = 1; i < ProtocolV1.HAND_SIZE + 1; i++) {
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
         } while(!splittedCommand[0].equals(ProtocolV1.END_GAME));
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
      for (int i = 1; i < toShow.length; i++) {
         System.out.print(toShow[i] + " ");
      }
      System.out.println("");
   }

   public void connect(int no) throws IOException, NoSuchAlgorithmException{
      Server server = (Server) servers.toArray()[no];
      
      connect(server);
   }
   
   /**
    * Méthode pour réaliser la connexion
    * @param server Le serveur à initialiser
    * @throws IOException Erreur si on refuse la connexion
    */
   public void connect(Server server) throws IOException, NoSuchAlgorithmException {
      if (!isConnected()) {
         clientSocket = new Socket(server.getIpAddress(), ProtocolV1.PORT);
         clientSocket.setSoTimeout(TIMEOUT_ANSWER);
         
         responseBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
         byteWriter = clientSocket.getOutputStream();
         writer = new PrintWriter(new OutputStreamWriter(byteWriter, "UTF-8"));
         
         String[] answer;
         do {
             // On lit la première réponse du serveur
             answer = (responseBuffer.readLine()).split(ProtocolV1.SEPARATOR);

            switch (answer[0]) {
               case ProtocolV1.ACCEPT_CONNECTION:
                  connected = true;
                  playerNumber = Integer.valueOf(answer[1]);
                  /* Dés que la partie commence on crée un thread qui écoute les messages
                     envoyés par le chat.
                  */
                  chatClient = new ChatClient(clientSocket.getInetAddress().getHostAddress(),
                                              playerNumber);
                  chatClient.listen();
                  break;
               case ProtocolV1.REFUSE_CONNECTION:
                  System.out.println("Connection Refusee");
                  break;
               case ProtocolV1.HASH:
                  System.out.println("Please enter the password : ");
                  Scanner keyboard = new Scanner(System.in);
                  String password = keyboard.nextLine();
                  byte[] hashedPassword = hash(password);               
                  writer.println(ProtocolV1.HASH);
                  writer.flush();
                  byteWriter.write(hashedPassword);
                  byteWriter.flush();
                  break;
               default:
                  System.out.println("reponse reçue: " + answer);
                  break;
            }
         } while (!(answer[0].equals(ProtocolV1.ACCEPT_CONNECTION) || 
                    answer[0].equals(ProtocolV1.REFUSE_CONNECTION)));
      }
      else {
         System.out.println("deja connecte");
      }
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
            for (Integer i : cards) {
               readLineCardFileInfo(i);
            }
            int positionCard = in.nextInt();
            if (positionCard <= 0 || positionCard > ProtocolV1.HAND_SIZE) {
               continue;
            }
            cardChoice = cards.get(positionCard - 1);
            break;
         } catch (InputMismatchException e) {
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
         while ((line = buff.readLine()) != null) {
            if (counter == lineNo) {
               String[] infosCard = line.split(";");
               System.out.println(infosCard[1]);
               return Integer.parseInt(infosCard[0]);
            }
            counter++;
         }
         buff.close();
      } catch (IOException | NumberFormatException e) {
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

      while (true) {
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
   
   public void sendEmoticon(Emoticon emoticon)
   {
      chatClient.write(emoticon);
   }
   
   public void displayEmoticon(int player, Emoticon emoticon)
   {
      ui.showEmoticon(player, emoticon);
   }

//   public static void main(String... args) {
//      Player player = new Player(args[0]);
//      player.getServer();
//   }
   public static void main(String... args) {
      Player player = new Player(args[0]);
      boolean entryOk = false;
      while (!entryOk) {
         entryOk = true;
         System.out.println("souhaitez-vous creer ou rejoindre un server ? (c/r)");
         Scanner in = new Scanner(System.in);
         String answer = in.nextLine();
         String answerNameServer = defaultServerName;
         if (answer.equals("c")) {
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
                  catch(IOException ex)
                  {
                     System.out.println(ex.getMessage());
                  }
               }
            } while(true);
            
         } else if (answer.equals("r")) {
            while (!player.connected) {
               player.getServers();
               player.showServers();
               System.out.println("quel est le numero du serveur que vous souhaitez ? (-1 pour terminer)");
               try {
                  int answerNum = Integer.parseInt(in.nextLine());
                  player.connect(answerNum);
               } catch (IOException ex) {
                  System.out.println(ex + ": le numero n'est pas valide");
               } catch(NumberFormatException NFE) {
                  System.out.println(NFE + ": le numero n'est pas valide");
               } catch (NoSuchAlgorithmException ex) {
                  System.out.println("Algorithme de hash invalide : " + ex.getMessage());
               }
            }
         } else {
            entryOk = false;
            System.out.println("fausse commande");
         }
         
         player.startGame();
      }
   }
}
