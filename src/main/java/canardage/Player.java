package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

/**
 *
 */
public class Player {
   
   private final int HAND_CARDS_NUMBER = 3;
   
   private Socket clientSocket;
   private BufferedReader responseBuffer;
   private PrintWriter writer;

   private int[] cards = new int[ProtocolV1.HAND_SIZE];

   boolean connected = false;

   public Player(String adress) {
      try {
         connect(adress);
      } catch (IOException e) {
         System.out.println(e.toString());
      }
   }

   private void startGame() throws IllegalStateException {
      if (isConnected()) {
         
         String inputServer;
         String[] splittedCommand;
         
         do {
            try {
               inputServer = responseBuffer.readLine();
               splittedCommand = inputServer.split(ProtocolV1.SEPARATOR);
            } catch(IOException e) {
               System.out.println(e.toString());
               continue;
            } catch(PatternSyntaxException e) {
               System.out.println(e.toString());
               continue;
            }
            
            switch (splittedCommand[0]){
               case ProtocolV1.ASK_FOR_POSITION:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
               case ProtocolV1.DISTRIBUTE_CARD:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
               case ProtocolV1.DISTRIBUTE_HAND:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
               case ProtocolV1.PATCH_BOARD:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
               case ProtocolV1.YOUR_TURN:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
               case ProtocolV1.REFUSE_CARD:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
               case ProtocolV1.:
                  writer.println(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
            }
         }while(!splittedCommand[0].equals(ProtocolV1.END_GAME));
      } else {
         throw new IllegalStateException("vous devez vous connecter à un serveur avant de pouvoir commencer une partie");
      }
   }

   public void connect(String adress) throws IOException {
      if (!isConnected()) {
         clientSocket = new Socket(adress, ProtocolV1.PORT);

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
   }

   public boolean isConnected() {
      return connected;
   }

   public int getLocationChoice() {
      Scanner in = new Scanner(System.in);
      int positionChoice;

      // Loop while the user choose a bad move
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
}
