package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import duckException.BadGameInitialisation;
import java.io.*;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
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
   private final long refreshDelay = 2000;

   private final String ENCODING_ALGORITHM = "SHA-256";
   private final String FORMAT_TEXT = "UTF-8";

   private Socket clientSocket;
   private BufferedReader responseBuffer;
   private PrintWriter writer;
   private Set<Server> servers;

   private List<Integer> cards;

   boolean connected = false;

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
         //DatagramSocket socket = new DatagramSocket(ProtocolV1.MULTICAST_PORT, InetAddress.getByName("0.0.0.0"));
         boolean messageRed = false;
         long start = new Date().getTime();
         while (new Date().getTime() - start < refreshDelay) {
            System.out.println(new Date().getTime() - start);
            byte[] buffer = new byte[2048];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
            System.out.println("ok");
            socket.receive(datagram);
            System.out.println("ok2");
            String msg = new String(datagram.getData());
            System.out.println(msg);
            msg = msg.substring(0, msg.lastIndexOf('}') + 1);
            Gson gson = new Gson();
            Type type = new TypeToken<Server>() {}.getType();
            if(!servers.contains((Server)gson.fromJson(msg, type))) {
               servers.add((Server)gson.fromJson(msg, type));
            }
         }
         socket.leaveGroup(InetAddress.getByName(ProtocolV1.MULTICAST_ADDRESS));
      } catch (SocketException ex) {
         System.out.println("socket creation fail");
      } catch (IOException ex) {
         System.out.println("read broadcast fail");
      }
   }

   private byte[] hash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      MessageDigest md = MessageDigest.getInstance(ENCODING_ALGORITHM);
      md.update(password.getBytes(FORMAT_TEXT));
      return md.digest();
   }

   public void createServer(String name, String password) {
      byte[] hash;
      try {
         hash = hash(password);
      } catch (NoSuchAlgorithmException ex) {
         System.out.println("bad choice of hash algorithm");
         return;
      } catch (UnsupportedEncodingException ex) {
         System.out.println("bad choice of text format");
         return;
      }

      ServerManager server = new ServerManager(name, hash);
      if (!server.isRunning()) {
         try {
            server.acceptClients();
         } catch (IOException e) {
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

   /**
    *
    * @param adress
    * @throws IOException
    */
   public void connect(int no) throws IOException {
      if (!isConnected()) {
         Server server = (Server) servers.toArray()[no];
         clientSocket = new Socket(server.getIpAddress(), ProtocolV1.PORT);
         
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
      } catch (Exception e) {
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
            do {
               System.out.println("'go' pour commencer!!!");
               answer = in.next();
               if(answer.equals("go")) {
                  try {
                  //startGame
                     break;
                  } catch(BadGameInitialisation e) {
                     
                  }
               }
            } while(true);
            
         } else if (answer.equals("r")) {
            int answerNum = -2;
            while (!player.connected) {
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
         
         player.startGame();
      }
   }
}
