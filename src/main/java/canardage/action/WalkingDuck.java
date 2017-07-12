
package canardage.action;

public class WalkingDuck extends WithPlayer {

   private static final int ID;
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public int getNbCards() {
      return 3;
   }

   @Override
   public int getID() {
      return ID;
   }

   @Override
   public void effect() {
      if(hasEffect()) {
         int idPlayerChoice = getIDPlayerChoice();
         board.reviveDuck(idPlayerChoice);
      }
   }

   @Override
   public String getFile() {
      return "/images/CardWalkingDuck.jpg";
   }

   @Override
   public boolean isPlayable(int idPlayer) {
      if(board.hasDeadDuck(idPlayer)) {
         return true;
      }
      return false;
   }
   
}
