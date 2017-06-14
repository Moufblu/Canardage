package chat;

import javafx.scene.image.Image;

/**
 * Description: Classe pour faire un enum des émoticônes
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
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
