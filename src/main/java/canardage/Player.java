package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Player {

   private final int HAND_CARDS_NUMBER = 3;
   private final long REFRESH_DELAY = 2000;

   private final String ENCODING_ALGORITHM = "SHA-256";
   private final String FORMAT_TEXT = "UTF-8";

   private Socket clientSocket;
   private BufferedReader responseBuffer;
   private PrintWriter writer;
   private Set<Server> servers;

   private List<Integer> cards;

   boolean connected = false;
   
   private final static String defaultServerName = "Canardage";
   private final static String defaultPassword = "";

   /**
    *
    * @param adress
    */
   public Player(String adress) {
      cards = new ArrayList<>();
      servers = new HashSet<>();
   }

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

   private byte[] hash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      MessageDigest md = MessageDigest.getInstance(ENCODING_ALGORITHM);
      md.update(password.getBytes(FORMAT_TEXT));
      return md.digest();
   }

   public ServerManager createServer(String name, String password) {
      byte[] hash;

      try {
         hash = hash(password);
      } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
         throw new RuntimeException(ex.getCause());
      }

      ServerManager server = new ServerManager(name, hash);
      if (!server.isRunning()) {
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
                  if (cards.size() > 0) {
                     throw new IllegalStateException("Action Cards of player full yet");
                  } else {

                     cards.add(readLineCardFileInfo(Integer.parseInt(splittedCommand[1])));
                  }
                  break;
               case ProtocolV1.DISTRIBUTE_HAND:
                  if (cards.size() > 0) {
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
         } while (!splittedCommand[0].equals(ProtocolV1.END_GAME));
      } else {
         throw new IllegalStateException("vous devez vous connecter à un serveur avant de pouvoir commencer une partie");
      }
   }

   /**
    *
    * @param toShow
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
    *
    * @param server
    * @throws IOException
    */
   public void connect(Server server) throws IOException, NoSuchAlgorithmException {
      if (!isConnected()) {
         clientSocket = new Socket(server.getIpAddress(), ProtocolV1.PORT);
         
         responseBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
         writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

         //We read the first answer from the server
         String answer = responseBuffer.readLine();
         
         switch (answer) {
            case ProtocolV1.ACCEPT_CONNECTION:
               connected = true;
               break;
            case ProtocolV1.REFUSE_CONNECTION:
               System.out.println("Connection Refusee");
               break;
            case ProtocolV1.HASH:
               Scanner keyboard = new Scanner(System.in);
               String password = keyboard.nextLine();
               byte[] hashedPassword = hash(password);               
               writer.println(new String(hashedPassword, StandardCharsets.UTF_8));
               break;
            default:
               System.out.println("reponse reçue: " + answer);
               break;
         }
      }
      else {
         System.out.println("deja connecte");
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
