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

   public Joueur() {
      try {
         connect();
      } catch (IOException e) {
         System.out.println(e.toString());
      }
   }

   private void startGame() throws IllegalStateException {
      if (isConnected()) {
         do {
            // readline
            switch (/*premier mot*/){
               case ProtocolV1.POSITION:
                  writeline(ProtocolV1.messageAskPosition(getLocationChoice()));
                  break;
            }
         }while(true); // arrêt si doit arrêter
      } else {
         throw new IllegalStateException("vous devez vous connecter à un serveur avant de pouvoir commencer une partie");
      }
   }

   public void connect() throws IOException {
      if (!isConnected()) {
         clientSocket = new Socket(ProtocolV1., port);

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
