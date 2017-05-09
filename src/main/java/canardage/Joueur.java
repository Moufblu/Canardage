
package canardage;

import java.net.Socket;
import Protocol.ProtocolV1;

/**
 *
 */
public class Joueur {
   
   private final int HAND_CARDS_NUMBER = 3;
   
   private Socket clientSocket;
   private int[] cards = new int[HAND_CARDS_NUMBER];
   
   
}
