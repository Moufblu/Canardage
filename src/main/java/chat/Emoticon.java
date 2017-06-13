package chat;

import javafx.scene.image.Image;

public enum Emoticon {
   EMOTE1("/images/Content.png"),
   EMOTE2("/images/Triste.png"),
   EMOTE3("/images/Please.png"),
   EMOTE4("/images/Pan.png");

   Image emote;
   
   private Emoticon(String imagePath) {
      emote = new Image(imagePath);
   }

   public Image getEmote() {
      return emote;
   }
}
