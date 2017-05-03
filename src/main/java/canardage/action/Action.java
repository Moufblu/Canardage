package canardage.action;

import canardage.Board;

/**
 * root class to inherit from when we want to add a card/effect
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
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
      if(player < 0 || player >= board.getMaxPlayers()) {
         throw new IllegalArgumentException("Le numéro de joueur est invalide.");
      }
      this.player = player;
   }
   
   public int getPlayer() {
      return player;
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
