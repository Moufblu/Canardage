package canardage.action;

/**
 * Description: Classe pour implémenter la carte qui protège une position sur le
 * plateau et qui reste défendue pendant un tour
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Guard extends WithLocation {
   
   private static final int ID;  // Id de la carte
   
   static {
      ID = Action.counter++;
   }
   
   /**
    * Surcharge de la méthode 'getId()' pour l'obtention de l'ID de la carte
    * @return L'ID de la carte
    */
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

   /**
    * Surcharge de la méthode 'getNbCards()' pour nous donner le nombre de cartes de 
    * disponibles de ce type
    * @return Le numéro de la carte
    */
   @Override
   public int getNbCards() {
      return 3;
   }

   /**
    * Surcharge de la méthode 'getFile()' pour obtenir le chemin vers l'image de la 
    * carte
    * @return Le chemin vers cett image
    */
   @Override
   public String getFile() {
      return "/images/CardACouvert.jpg";
   }
}
