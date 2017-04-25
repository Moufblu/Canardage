package canardage;

import java.util.Collections;
import java.util.List;
import java.lang.IndexOutOfBoundsException;

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
   private final static int WATER_CARD_VALUE = 0;
   private final static int MAX_DUCK_PER_PLAYER = 5;
   
   private List<Integer> ducks;
   private final static int NB_LOCATIONS = 6;
   private final Location[] locations;

   /**
    * 
    * @param nbPlayers nombre de joueurs accepte
    */
   public Board(int nbPlayers) {
      /* calcul du nombre de carte eau selon le nombre de joueur */
      int nbCardsWater = 0;
      if(nbPlayers == 2) {
         nbCardsWater = 2;
      } else {
         nbCardsWater = nbPlayers - 1;
      }
      
      /* ajout des cartes eau */
      for(int i  = 0; i < nbCardsWater; i++) {
         ducks.add(WATER_CARD_VALUE);
      }
      
      /* ajout des cartes canards */
      int  cpt = 1;
      for(int i  = 0; i < nbPlayers; i++) {
         for(int j  = 0; j < MAX_DUCK_PER_PLAYER; j++) {
            ducks.add(cpt);
         }
         cpt++;
      }
      
      /* melange des cartes */
      shuffleAll();
      /* place les canards sur la partie visible du plateau */
      placeDucks();
      
      locations = new Location[NB_LOCATIONS];
      placeDucks();
   }
   
   public void placeDucks(){
      //nadir
       for (int i = 0; i < NB_LOCATIONS; i++)
       {
           placeDuck();
       }
   }
   
   public void pushBack(int duck){
      //nadir
       ducks.add(duck);
   }
   
   public boolean swap(int location, boolean left) {
      //nadir       
      if (left) {
         if (location < NB_LOCATIONS - 1 && location >= 0) {
            swap(location, location + 1);
         } else {
            return false;
         }
      } else {
         if (location < NB_LOCATIONS && location > 0) {
            swap(location, location - 1);
         } else {
            return false;
         }
      }
      
      return true;
   }
   
   public void swap(int location1, int location2){
      //nadir
      validate(location1);
      validate(location2);
      
      int duckTemp       = locations[location1].duck,
          hiddenDuckTemp = locations[location1].hiddenDuck;

      locations[location1].duck = locations[location2].duck;
      locations[location1].hiddenDuck = locations[location2].hiddenDuck;

      locations[location2].duck = duckTemp;
      locations[location2].hiddenDuck = hiddenDuckTemp;
   }
   
  /**
   * cache un canard derriere un autre
   * @param location position du canard qui souhaite se cacher
   * @param left true si on a choisit de se cacher a gauche
   */
   public void hide(int location, boolean left){
      
   }
   /**
    * melange la liste ducks mais pas les positions visibles
    */
   public void shuffleAll(){
      Collections.shuffle(ducks);
   }
   /**
    * enleve les canards des position visible et les places dans la liste ducks
    */
   public void retrieveDucks(){
      
   }
   
   public void setTarget(int posDuck, boolean value){
      validate(posDuck);
      
      if (locations[posDuck].duck > 0)
      {
         locations[posDuck].targetted = value;
      }
   }
   
   public boolean isTargetted(int location)
   {
      validate(location);
      return locations[location].targetted;
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
   
   private void validate(int location)
   {
      if (!(location >= 0 && location < NB_LOCATIONS))
      {
         throw new IndexOutOfBoundsException("Location out of bounds !");
      }
   }
}
