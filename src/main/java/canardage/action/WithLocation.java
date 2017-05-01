/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import java.util.Scanner;

/**
 *
 * @author jimmy
 */
public abstract class WithLocation extends Action {

   public WithLocation() {
      super();
   }
   
   /**
    * to Override.
    * check if we the choosen location is playable.
    * @param position the number of the location
    * @return true if we can play at this location
    */
   public abstract boolean isPlayable(int position);
   
   /**
    * ask a location in console (to be modified later)
    * @return the choosen number of the location
    */
   protected int getLocationChoice() {
      Scanner in = new Scanner(System.in);
      int positionChoice;
      
      // Loop while the user choose a bad move
      while(true) {
         positionChoice = in.nextInt();
         if(isPlayable(positionChoice)) {
            break;
         }
      }
      return positionChoice;
   }
   
   /**
    * 
    * @return 
    */
   @Override
   public boolean hasEffect() {
      for(int i = 0; i < board.NB_LOCATIONS; i++) {
         if(isPlayable(i)) {
            return true;
         }
      }
      return false;
   }
}
