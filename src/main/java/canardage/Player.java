package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Description: Classe pour créer et gérer les joueurs d'une partie
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Player {
   
   private final int HAND_CARDS_NUMBER = 3;  // Nombre de cartes maximal d'un joueur
   private final long refreshDelay = 2000;
   
   private final String ENCODING_ALGORITHM = "SHA-256";  // Algorithme de hachage
   private final String FORMAT_TEXT = "UTF-8";  // Format d'encodage du texte
   
   private Socket clientSocket;           // Socket du client
   private BufferedReader responseBuffer; // Buffer pour le réponse
   private PrintWriter writer;            // Writer pour les envois d'informations
   private Set<Server> servers;           // La liste des serveurs

   private List<Integer> cards;           // Liste des cartes

   boolean connected = false;             // Booléen pour connaître la connexion

   /**
    * Constructeur de la classe Player
    */
   public Player() {
      cards = new ArrayList<>();
      servers = new HashSet<>();
   }

   /**
    * Méthode pour obtenir les serveurs disponibles
    */
   public void getServers() {
      boolean socketInitOk = false;
      while (!socketInitOk) {
         MulticastSocket socket;
         try {
            socket = new MulticastSocket(ProtocolV1.MULTICAST_PORT);
            socket.joinGroup(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
            boolean messageRed = false;
            long start = new Date().getTime();
            long now = new Date().getTime();
            while (now - start < refreshDelay) {
               byte[] buffer = new byte[2048];
               DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
               System.out.println("ok");
               socket.receive(datagram);
               System.out.println("ok2");
               String msg = new String(datagram.getData());
               msg = msg.substring(0, msg.lastIndexOf('}') + 1);
               Gson gson = new Gson();
               Type type = new TypeToken<Server>() {}.getType();
               System.out.println(msg + "#");
               servers.add((Server)gson.fromJson(msg, type));
               now = new Date().getTime();
            }
            socket.leaveGroup(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
         } catch(SocketException ex) {
            System.out.println("Erreur à la création du Socket.");
            System.out.println("socket creation fail");
         } catch(IOException ex) {
            System.out.println("Erreur à la lecture du Broadcast.");
            System.out.println("Read broadcast fail");
         }
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
   public void createServer(String name, String password) {
      byte[] hash;
      try {
         hash = hash(password);
      } catch(NoSuchAlgorithmException ex) {
         System.out.println("bad choice of hash algorithm");
         return;
      } catch(UnsupportedEncodingException ex) {
         System.out.println("bad choice of text format");
         return;
      }

      ServerManager server = new ServerManager(name, hash);
      if(!server.isRunning()) {
         try {
            server.startServer();
         } catch(IOException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   public void showServers() {
      for (Server s : servers) {
         System.out.println(s.toString());
      }
   }

   /**
    * Intialisation de la partie
    * @throws IllegalStateException Lancée si trop de cartes dans la main du joueur, 
    * si pas de cartespas de connexion avec le serveur
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
               System.out.println(e.toString());
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
//                     throw new IllegalStateException("Action Cards of player full yet");
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

   /**
    * Méthode pour réaliser la connexion
    * @param no Le numéro du serveur à initialiser
    * @throws IOException Erreur si on refuse la connexion
    */
   public void connect(int no) throws IOException {
      if(!isConnected()) {
         Server server = (Server)servers.toArray()[no];
         clientSocket = new Socket(server.getIpAddress(), ProtocolV1.PORT);

         responseBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
         writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

         //We read the first answer from the server
         // On lit la première réponse du serveur
         String answer = responseBuffer.readLine();
         // Ici ça propose un switch, on transforme en switch? (pas la console, on est pauvre)
         if(answer.equals(ProtocolV1.ACCEPT_CONNECTION)) {
            connected = true;
         } else if(answer.equals(ProtocolV1.REFUSE_CONNECTION)) {
            System.out.println("Connexion refusée.");
            System.out.println("Connection Refusee");
         } else {
            System.out.println("Réponse reçue: " + answer);
         }
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

      // Loop while the user choose a bad move
      // Boucke tant que le joueur ne choisi pas le bon numéro d'une carte
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
   
   // Not sure of what I'm saying :/
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
      } catch (Exception e){ // Pourquoi une exception ici (qui en plus veut rien dire) 
                             // et qui n'est pas dans le throw? Puis Java propose des erreurs
                             // Jimmy, Alt + Enter, pls
         System.out.println(e.toString());
      }
      throw new IllegalArgumentException("Numéro de carte invalide.");
//      throw new IllegalArgumentException("card number not valid");
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
            // Pourquoi les positions entre 0 et 5 et pas 1 et 6 comme pour les cartes? :o
            // Seems legit if 1-6 instead of 0-5 (?)
            System.out.println("Veuillez entrer une position valide : (0..5)");
            positionChoice = in.nextInt();
            break;
         } catch(InputMismatchException e) {
            System.out.println("Entrée non valide");
         }
      }
      return positionChoice;
   }

//   public static void main(String... args) {
//      Player player = new Player(args[0]);
//      player.getServer();
//   }
   
   /**
    * Ara ma... mein no comento wa hitsuyou nanda? Soretomo, do demo ii?
    * Commentaires dans le main nécessaire ou fuck it?
    * @param args Nani kono kuso mushi?
    */
   public static void main(String... args) {
      Player player = new Player();
      boolean entryOk = false;
      while (!entryOk) {
         entryOk = true;
         System.out.println("souhaitez-vous creer ou rejoindre un server ? (c/r)");
         Scanner in = new Scanner(System.in);
         String answer = in.next();
         String answerNameServer = "";
         if (answer.equals("c")) {
            boolean nameNotRedondant = false;
            while (!nameNotRedondant) {
               nameNotRedondant = true;
               //player.getServers();
               System.out.println("quel est le nom du serveur ?");
               answerNameServer = in.next();
               for (Server server : player.servers) {
                  if (server.getName().equals(answerNameServer)) {
                     nameNotRedondant = false;
                  }
               }
            }
            System.out.println("quel est le mot de passe ?");
            String answerPassword = in.next();
            player.createServer(answerNameServer, answerPassword);
         } else if (answer.equals("r")) {
            int answerNum = -2;
            while (answerNum != -1) {
               player.getServers();
               player.showServers();
               System.out.println("quel est le numero du serveur que vous souhaitez ? (-1 pour terminer)");
               answerNum = in.nextInt();
               try {
                  player.connect(answerNum);
               } catch (IOException ex) {
                  answerNum = -2;
                  System.out.println("le numero n'est pas valide");
               }
            }
         } else {
            entryOk = false;
            System.out.println("fausse commande");
         }
      }
   }
}
