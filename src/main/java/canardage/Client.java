/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author DiasMi
 */
public class Client {

   private Socket clientSocket;
   private BufferedReader reader;
   private PrintWriter writer;

   public Client(Socket clientSocket) throws IOException {
      this.clientSocket = clientSocket;
      reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
   }

   void writeLine(String message) {
      writer.println(message);
      writer.flush();
   }

   String readLine() throws IOException {
      return reader.readLine();
   }
   
   void close() throws IOException{
      clientSocket.close();
   }

}
