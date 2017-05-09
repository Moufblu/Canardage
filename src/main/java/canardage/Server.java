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
   
   private List<Socket> playersSockets;
   
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
               Socket clientSocket = serverSocket.accept();
               playersSockets.add(clientSocket);
               nbPlayers++;
               
               BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
               PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
               
               writer.println(ProtocolV1.ACCEPT_CONNECTION);
               writer.flush();
               
               int[] hand = {1, 2, 3};
               writer.println(ProtocolV1.messageHand(hand));
               writer.flush();
               
               writer.println(ProtocolV1.ASK_FOR_POSITION);
               writer.flush();
               
               String answer = reader.readLine();
               if (answer.contains(ProtocolV1.ASK_FOR_POSITION))
               {
                  System.out.println("Tout va bien");
               }
               else
               {
                  System.out.println("DEGUEU");
               }
               
               writer.println(ProtocolV1.END_GAME);
               writer.flush();
               
               clientSocket.close();
               
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
}
