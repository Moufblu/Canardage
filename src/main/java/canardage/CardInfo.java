package canardage;

/**
 * Description: Classe pour l'information des cartes
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class CardInfo {

   private int position;
   private int idCard;
   private boolean hasEffect;

   public CardInfo(int position, int idCard, boolean hasEffect) {
      this.position = position;
      this.idCard = idCard;
      this.hasEffect = hasEffect;
   }

   public int getIdCard() {
      return idCard;
   }

   public int getPosition() {
      return position;
   }
   
   public boolean hasEffect() {
      return hasEffect;
   }

}
