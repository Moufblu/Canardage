package canardage.action;

/**
 * Description: Classe pour implémenter la carte de placer une cible sur le plateau
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Target extends WithLocation {

   /**
    * Vérifie si la carte a un effet et réalise l'effet correspondant si possible
    */
   @Override
   public void effect() {
      // Vérifie si l'effet est possible à chaque position
      if(hasEffect()) {
         board.setTarget(getLocationChoice(), true);
      }
   }

   /**
    * Vérifie si la carte de mettre une cible peut être jouée
    * @param position La postion sur laquelle on veut jouer la carte
    * @return Vrai si c'est possible, faux sinon
    */
   @Override
   public boolean isPlayable(int position) {
      if(!board.isTargetted(position)) {
         return true;
      }
      return false;
   }
}
