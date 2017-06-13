
package canardage;

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
