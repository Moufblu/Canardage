package canardage.action;

import canardage.Board;

/**
 * Description: Classe à hériter quand on veut rajouter une carte ou un effet Date:
 * 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public abstract class Action {

   protected Board board;  // Où l'effet ce réalisera
   protected int player;   // Joueur courrant
   protected static int counter = 0;

   /**
    * Constructeur de la classe Action
    */
   public Action() {
      Board.registerInstance(3);
      board = Board.getInstance();
   }
   public abstract int getNbCards();
   public abstract int getId();

   /**
    * Méthode qui défini le joueur courrant
    * @param player Le numéro du joueur courrant
    * @throws IllegalArgumentException Si le numéro du joueur est invalide
    */
   public void setPlayer(int player) throws IllegalArgumentException {
      if(player < 0 || player >= board.getMaxPlayers()) {
         throw new IllegalArgumentException("The player's number is invalid.");
      }
      this.player = player;
   }

   /**
    * Méthode pour obtenir le joueur courrant
    * @return Le joueur courrant
    */
   public int getPlayer() {
      return player;
   }
   
   //public static abstract int getId();

   /**
    * Méthode à redéfinir dans les sous-classes pour implémenter un nouveau effet
    */
   public abstract void effect();

   /**
    * Méthode à redéfinir dans les sous-classes pour savoir si une carte à un effet
    * @return Faux si la méthode ne possède pas d'effet sur le plateau au moment de
    * son utilisation par le joueur
    */
   public abstract boolean hasEffect();
}
