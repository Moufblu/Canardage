package canardage;

import java.net.ServerSocket;
import Protocol.ProtocolV1;
import java.io.IOException;
import java.net.InetAddress;

/**
 *
 */
public class Server {

   private final static int MAX_NB_PLAYERS = 6;
   ServerSocket serverSocket;

   private int[] actionCards;
   private int[][] playerCards;

   private int nbPlayers;

   public Server() {
      new Thread() {

         @Override
         public void run() {
            try {
               serverSocket = new ServerSocket(ProtocolV1.PORT, MAX_NB_PLAYERS, InetAddress.getByName(ProtocolV1.ADDRESS));
            } catch (IOException e) {

            }
         }

      }.run();
   }

}
