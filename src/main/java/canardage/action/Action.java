package canardage.action;

import canardage.Board;
import java.util.Scanner;

/**
 * root class to inherit from when we want to add a card/effect
 */
public abstract class Action {
   
   protected Board board;
   protected int player;
   
   /**
    * constructor
    * @param board where the effect will be done
    */
   public Action() {
      board = Board.getInstance();
   }
   
   public void setPlayer(int player) throws IllegalArgumentException {
      if(player < 0 || player >= board.nbPlayer) {
         throw new IllegalArgumentException("le numero de joueur est invalide");
      }
      this.player = player;
   }
   
   /**
    * metode that has to be override when implement a new effect card/class
    */
   public abstract void effect();
   
   /**
    * @return false if the effect metode has no effects on the board actually
    */
   public abstract boolean hasEffect();
   
   
}
