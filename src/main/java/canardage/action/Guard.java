package canardage.action;

/**
 * Description: Classe pour implémenter la carte qui protège une position sur le
 * plateau et qui reste défendue pendant un tour Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Guard extends WithLocation {
   private static final int ID;
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public int getID() {
      return ID;
   }
   
   /**
    * On regarde si la carte de garde peut utiliser son effet et on l'utilise si
    * possible
    */
   @Override
   public void effect() {
      if(hasEffect()) {
         board.setGuard(getLocationChoice(), true);
      }
   }

   /**
    * Vérifie si la carte de protection peut être jouée
    * @param position La postion sur laquelle on veut jouer la carte
    * @return Vrai si c'est possible, faux sinon
    */
   @Override
   public boolean isPlayable(int position) {
      return !board.isGuarded(position);
   }

   @Override
   public int getNbCards() {
      return 3;
   }

   @Override
   public String getFile() {
      return "/images/CardACouvert.jpg";
   }
}
