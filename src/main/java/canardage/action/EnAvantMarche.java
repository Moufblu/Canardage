/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

/**
 *
 * @author jiver
 */
public class EnAvantMarche extends Action {

   private static final int ID;  // Id de la carte
   
   static {
      ID = Action.counter++;
   }
   
   @Override
   public int getNbCards() {
      return 50;
   }

   @Override
   public int getID() {
      return ID;
   }

   @Override
   public void effect() {
      board.enAvantMarche();
   }

   @Override
   public boolean hasEffect() {
      return true;
   }

   @Override
   public String getFile() {
      return "/images/CardEnAvantMarche.jpg";
   }
   
}
