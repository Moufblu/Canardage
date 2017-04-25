package canardage;

import java.util.Collections;
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
   public boolean hide(int location, boolean left){
      boolean verify = true;
      int locationWish = 0;
      if(left) {
         locationWish = location + 1;
      }
      else {
         locationWish = location - 1;
      }
      
      //verifie que les condition d'un hide soit correct
      //qu'il n'y a pas de canard caché sous les canard
      if(locations[locationWish].hiddenDuck != MISSING) {
         verify = false;
      }
      if(locations[location].hiddenDuck == MISSING) {
         verify = false;
      }
      //qu'il y ait des canard sous les positions
      if(locations[location].duck == MISSING) {
         verify = false;
      }
      if(locations[locationWish].duck == MISSING) {
         verify = false;
      }
      //que la position souhaitée est dans le range possible
      if(locationWish < 0 && locationWish >= NB_LOCATIONS) {
         verify = false;
      }
      
      //cache le canard
      if(verify) {
         locations[locationWish] = removeDuck(location);
      }
      
      return verify;
   }
   
   /**
    * melange la liste ducks mais pas les positions visibles
    */
   public void shuffleAll(){
      Collections.shuffle(ducks);
   }
   
   /**
    * enleve les canards des positions visibles et les places dans la liste ducks
    */
   public void retrieveDucks(){
      for(int i = 0; i < 2; i++) {
         for(int j = 0; j < NB_LOCATIONS; j++) {
            int temp = removeDuck(j);
            if(temp != MISSING) {
               ducks.add(temp);
            }
         }
      }
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
   
   public static void main(String... args) {
      
   }
}


