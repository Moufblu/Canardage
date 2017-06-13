package canardage.action;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Description: Classe pour implémenter la carte de tirer sur un canard ciblé Date:
 * 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Shoot extends WithLocation {
   
   private static final int ID;
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public int getID() {
      return ID;
   }
   
   /**
    * Vérifie si la carte a un effet et réalise l'effet correspondant si possible
    */
   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.fire(positionChoice);
      }
   }

   /**
    * Vérifie si la carte de tir peut être jouée
    * @param position La postion sur laquelle on veut jouer la carte
    * @return Vrai si c'est possible, faux sinon
    */
   @Override
   public boolean isPlayable(int position) {
      if(board.isTargetted(position)) {
//         Media sound = new Media(new File("/sounds/PAN.mp3").getAbsolutePath());
//         MediaPlayer mediaPlayer = new MediaPlayer(sound);
//         mediaPlayer.play();
         return true;
      }
      return false;
   }

   @Override
   public int getNbCards() {
      return 13;
   }
   
   @Override
   public String getFile() {
      return "/images/CardPan.jpg";
   }
}
