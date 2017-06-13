package chat;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum Emoticon {
   EMOTE1("/images/Content.png"),
   EMOTE2("/images/Triste.png"),
   EMOTE3("/images/Please.png"),
   EMOTE4("/images/Pan.png");

   ImageView emote;
   
   private Emoticon(String imagePath) {
      emote = new ImageView(new Image(imagePath));
   }

   public ImageView getEmote() {
      return emote;
   }
}
