package canardage;

import java.net.ServerSocket;
import Protocol.ProtocolV1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
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
   
   public Server()
   {
      deck = new ArrayList<>(NB_ACTION_CARDS);
      playerCards = new ArrayList<>();
      playersSockets = new ArrayList<>();
      nbPlayers = 0;
   }

   public void startServer() throws IOException 
   {
    if (serverSocket != null || serverSocket.isBound()) 
    {
       serverSocket = new ServerSocket(ProtocolV1.PORT, MAX_NB_PLAYERS);
    }

    Thread serverThread = new Thread(new Runnable() {
      @Override
      public void run() 
      {
         if (nbPlayers < MAX_NB_PLAYERS)
         {
            try
            {
               Client client = new Client(serverSocket.accept());
               playersSockets.add(client);
               nbPlayers++;
               
               client.writeLine(ProtocolV1.ACCEPT_CONNECTION);
               
               int[] hand = {1, 2, 3};
               client.writeLine(ProtocolV1.messageHand(hand));
               
               client.writeLine(ProtocolV1.ASK_FOR_POSITION);
               
               String answer = client.readLine();
               if (answer.contains(ProtocolV1.ASK_FOR_POSITION))
               {
                  System.out.println("Tout va bien");
               }
               else
               {
                  System.out.println("DEGUEU");
               }
               
               client.writeLine(ProtocolV1.END_GAME);
               
               client.close();
               
            }
            catch(IOException e)
            {
               System.out.println(e.getMessage());
            }
         }
         
         //Uniquement pour itération 3
         if (nbPlayers == 2)
         {
            try
            {
               serverSocket.close();
               
            } catch(IOException e)
            {
               System.out.println(e.getMessage());
            }
         }
      }
    });
    serverThread.start();
  }
   
   public boolean isRunning()
   {
      return serverSocket.isBound();
   }
   
   public static void main(String ... args)
   {
      Server server = new Server();
      if (!server.isRunning())
      {
         try
         {
         server.startServer();
         }
         catch(IOException e)
         {
            System.out.println(e.getMessage());
         }
      }
   }
}
