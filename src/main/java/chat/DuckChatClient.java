package chat;

import canardage.Player;
import java.io.IOException;

public class DuckChatClient extends ChatClient {

   public DuckChatClient(String address, int id) throws IOException {
      super(address, id);
   }

   @Override
   protected void process(String text) {
      super.process(text);
      
      String[] answer = text.split(canardage.Global.ProtocolV1.SEPARATOR);
      if (answer[0].equals(canardage.Global.ProtocolV1.EMOTICON)) {
         int player = Integer.valueOf(answer[1]);
         //On récupère l'id de l'émoticone reçu et on stocke l'enum associé
         Emoticon emoticon = Emoticon.valueOf(answer[2]);

         Player.getInstance().displayEmoticon(player, emoticon);
      }
   }
   
}
