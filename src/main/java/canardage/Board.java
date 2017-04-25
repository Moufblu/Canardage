package canardage;

import java.util.List;

/**
 *
 */
public class Board {
   
   class Location {

      private boolean targetted;
      private boolean guarded;
      private int duck;
      private int hiddenDuck;

      public Location() {
         targetted = false;
         guarded = false;
         duck = MISSING;
         hiddenDuck = MISSING;
      }
      
      public int getDuck() {
         return duck;
      }

      public int getHiddenDuck() {
         return hiddenDuck;
      }

      public boolean isGuarded() {
         return guarded;
      }

      public boolean isTargetted() {
         return targetted;
      }

      public void setDuck(int duck) {
         this.duck = duck;
      }

      public void setGuarded(boolean guarded) {
         this.guarded = guarded;
      }

      public void setHiddenDuck(int hiddenDuck) {
         this.hiddenDuck = hiddenDuck;
      }

      public void setTargetted(boolean targetted) {
         this.targetted = targetted;
      }
   }
   
   private final static int MISSING = -1;
   
   private List<Integer> ducks;

   public Board(int nbPlayers) {
   }
   
   public void placeDucks(){
      
   }
   
   public void pushBack(){
      
   }
   
   public void swap(int location, boolean left){
      
   }
   
   public void swap(int location1, int location2){
      
   }
 
   public void hide(int location, boolean left){
      
   }
   
   public void shuffleAll(){
      
   }
   
   public void retrieveDucks(){
      
   }
   
   public void target(int duck){
      
   }
   
   public void guard(int position){
      
   }
   
   public void fire(int location){
      
   }

   @Override
   public String toString() {
      return super.toString(); //To change body of generated methods, choose Tools | Templates.
   }
   
   private void placeDuck(){
      
   }
}
