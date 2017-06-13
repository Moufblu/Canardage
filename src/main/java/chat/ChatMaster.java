package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatMaster
{
   private static final int TIMEOUT_ANSWER = 0;
   private final int MAXIMUM_CONNEXIONS;
   private final List<ChatWorker> workers;
   private final ServerSocket socket;
   
   public ChatMaster(String address, int maxConnexions) throws IOException
   {
      MAXIMUM_CONNEXIONS = maxConnexions;
      socket = new ServerSocket(canardage.Global.ProtocolV1.CHAT_PORT, MAXIMUM_CONNEXIONS, InetAddress.getByName(address));
      workers = new ArrayList<>();
   }

   public void accept()
   {
      new Thread(new Runnable(){
         @Override
         public void run() 
         {
            while (workers.size() < MAXIMUM_CONNEXIONS)
            {
               try
               {
                  Socket s = socket.accept();
                  s.setSoTimeout(TIMEOUT_ANSWER);
                  ChatWorker worker = new ChatWorker(ChatMaster.this, s);
                  workers.add(worker);
                  new Thread(worker).start();
               } catch (SocketTimeoutException ex)
               {
                  System.out.println("TIMEOUT SERVER !!!");
               } catch(IOException ex)
               {
                  //Si on n'a pas réussi à accepter le client, on log et on recommence
                  Logger.getLogger(ChatMaster.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         }
      }).start();
   }
   
   synchronized void sendToAll(String text)
   {
      //System.out.println("Envoye a tout le monde : " + text);
      for (ChatWorker worker : workers)
         worker.write(text);
   }
}
