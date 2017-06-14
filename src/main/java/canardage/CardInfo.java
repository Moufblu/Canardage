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

   public CardInfo(int position, int idCard) {
      this.position = position;
      this.idCard = idCard;
   }

   public int getIdCard() {
      return idCard;
   }

   public int getPosition() {
      return position;
   }

}
