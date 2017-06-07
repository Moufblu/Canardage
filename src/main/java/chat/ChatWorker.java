package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatWorker implements Runnable
{
   private final ChatMaster master;
   private final Socket socket;
   private final BufferedReader reader;
   private final PrintWriter writer;
   
   ChatWorker(ChatMaster master, Socket socket) throws UnknownHostException, IOException
   {
      System.out.println("Worker cree !");
      this.master = master;
      this.socket = socket;
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
   }

   @Override
   public void run()
   {
      write("Connected !"); //TODELETE
      master.sendToAll("COUCOU !"); //TODELETE WHEN IT WORKS
      while (!socket.isClosed())
      {
         String line;
         try
         {
           line = reader.readLine();
            System.out.println("Recu : " + line);
         } catch (IOException ex)
         {
            line = "KO";
            Logger.getLogger(ChatWorker.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         master.sendToAll(line);
      }
      
      //TODO System.out.println("I'm out !!!");
   }
   
   void write(String text)
   {
      writer.println(text);
      writer.flush();
   }
}