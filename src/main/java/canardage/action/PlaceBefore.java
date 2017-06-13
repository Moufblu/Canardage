package canardage.action;


/**
 * Description: Classe pour implémenter la carte qui sert à mettre un canard une
 * position derrière lui dsur le plateu s'il y a un canard derrière lui Date:
 * 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class PlaceBefore extends WithLocation {
   
   private static final int id;
   
   static {
      id = counter++;
   }
   
   @Override
   public int getId() {
      return id;
   }
   
   /**
    * Vérifie si la carte a un effet et réalise l'effet correspondant si possible
    */
   @Override
   public void effect() {
      if(hasEffect()) {
         int positionChoice = getLocationChoice();
         board.swap(positionChoice, false);
      }
   }

   /**
    * Vérifie si la carte pour se mettre une position derrière peut être jouée
    * @param position La postion sur laquelle on veut jouer la carte
    * @return Vrai si c'est possible, faux sinon
    */
   @Override
   public boolean isPlayable(int position) {
      if(position == 0) {
         return false;
      } else {
         return board.isDuck(position) && board.isDuck(position - 1) && board.isMyDuck(position, getPlayer().getId());
      }
   }

   /**
    * Vérifie à chaque case s'il y a le canard du joueur qui veut jouer la carte et
    * s'il y a un canard juste derrière celui-ci
    * @return Vrai si c'est possible d'utiliser l'effet de la carte, faux sinon
    */
   @Override
   public boolean hasEffect() {
      for(int i = 1; i < board.getNbLocations(); i++) {
         if(isPlayable(i)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public int getNbCards() {
      return 3;
   }
   
   
   @Override
   public String getFile() {
      return "/images/CardCanardBlase.jpg";
   }
}
