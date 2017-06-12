package chat;

import Protocol.ProtocolV1;
import canardage.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {
   
   private static final int TIMEOUT_ANSWER = 0;
   private final int id;
   private final Socket socket;
   private final BufferedReader reader;
   private final PrintWriter writer;
   
   public ChatClient(String address, int id) throws IOException
   {
      this.id = id;
      socket = new Socket(address, ProtocolV1.CHAT_PORT);
      socket.setSoTimeout(TIMEOUT_ANSWER);
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
   }
   
   public void write(Emoticon emoticon) {
      writer.println(ProtocolV1.messageChat(id, emoticon));
      writer.flush();
   }
   
   public void listen() {
      new Thread(new Runnable() {

         @Override
         public void run() 
         {
            while (true)
            {
               String line = "";
               try
               {
                  line = reader.readLine();
               } catch(SocketTimeoutException ex)
               { 
                  System.out.println("TIMEOUT CLIENT !!!");
               }catch (IOException ex)
               {
                  //J'ai pas reçu le message, c'est pas grave je recommence
                  //Je pourrai envoyer KO pour annoncer qu'il y a eu un problème
                  line = "KO";
                  Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
               }

               process(line);
            }
         }
      }).start();
   }
   
   protected void process(String text)
   {
      System.out.println(text);
   }
   
   public int getId()
   {
      return id;
   }
}
