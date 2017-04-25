package canardage;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Board {
   
   private class Location {

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
      
      public int removeDuck(){
         int removed = duck;
         duck = hiddenDuck;
         hiddenDuck = MISSING;
         return removed;
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
      
   }
   
   public void pushBack(){
      
   }
   
   public void swap(int location, boolean left){
      
   }
   
   public void swap(int location1, int location2){
      
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
   
   public void target(int duck){
      
   }
   
   public void setGuard(int location, boolean value){
      validate();
      
      locations[location].guarded = value;
   }
   
   public boolean isGuarded(int location){
      validate();
      return locations[location].guarded;
   }
   
   public void fire(int location){
      if(locations[location].targetted){
         removeDuck(location);
         untarget(location);
      }
   }

   @Override
   public String toString() {
      String display = "[";
      
      for (int i = 0; i < locations.length; i++) {
         if(i != 0){
            display += ", ";
         }
         display += locations[i].duck;
      }
      
      display += "]   [" + ducks.size() + "]";
      
      return display;
   }
   
   private void placeDuck(){
      locations[0].duck = ducks.remove(ducks.size() - 1);
   }
   
   private int removeDuck(int location){
      int duck = locations[location].removeDuck();
      advance(location);
      return duck;
   }
   
   private void advance(int location){
      for (int i = location; i != 0; i--) {
         if(locations[i].duck == MISSING){
            swap(i, false);
         }
         else{
            break;
         }
      }
      
      if(locations[0].duck == MISSING){
         placeDuck();
      }
   }
}
