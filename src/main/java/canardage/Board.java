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
   private final static int NB_PLAYERS_MIN = 2;
   private final static int NB_PLAYERS_MAX = 6;
   
   private List<Integer> ducks;
   private final static int NB_LOCATIONS = 6;
   private final Location[] locations;

   /**
    * 
    * @param nbPlayers nombre de joueurs accepte
    */
   public Board(int nbPlayers) throws IllegalArgumentException {
      
      if(nbPlayers < NB_PLAYERS_MIN || nbPlayers > NB_PLAYERS_MAX) {
         throw new IllegalArgumentException("number of players unvalid");
      }
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
         locations[locationWish].hiddenDuck = removeDuck(location);
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
   
   public static void main(String... args) {
      
      //creation de 2 plateau un a 2 joueur et un a 6
      Board board2Player = new Board(2);
      Board board6Player = new Board(6);
      
      System.out.println("plateau pour 2 joueurs :");
      board2Player.toString();
      System.out.println("");
      System.out.println("plateau pour 6 joueurs");
      System.out.println(board6Player);
      
      System.out.println("echange première et dernière carte");//X2 car 2 méthodes swap
      board6Player.swap(0, 5);
      System.out.println(board6Player);
      System.out.println("echange une carte et une carte en dehors du plateau");//X2 car 2 méthode swap
      try {
         board6Player.swap(0, 10);
      } catch (IndexOutOfBoundsException e) {
         System.out.println(e);
      }
      System.out.println(board6Player);
      System.out.println("placer une cible en 3");//il faut vérifier sur un canard(possible) et sur water(impossible)
      board6Player.setTarget(3, true);
      System.out.println(board6Player);
      System.out.println("placer une cible sur une cible");
      board6Player.setTarget(3, true);
      System.out.println(board6Player);
      System.out.println("placer une cible en dehors du plateau");
      board6Player.setTarget(10, true);
      System.out.println(board6Player);
      System.out.println("tirer alors qu'il y a une cible");//canard meurt et cible disparait /eau cible disparait
      board6Player.fire(3);
      System.out.println(board6Player);
      System.out.println("tirer alors qu'il n'y a pas de cible");//impossible
      board6Player.fire(4);
      System.out.println(board6Player);
      System.out.println("tirer en dehors du plateau");
      board6Player.fire(10);
      System.out.println(board6Player);
      System.out.println("proteger une position");
      board6Player.setGuard(3, true);
      System.out.println(board6Player);
      System.out.println("proteger une position déjà protégé"); //impossible
      board6Player.setGuard(3, true);
      System.out.println(board6Player);
      System.out.println("tirer sur une position protégé");
      board6Player.fire(3);
      System.out.println(board6Player);
      System.out.println("enlever une protection");
      board6Player.setGuard(3, false);
      System.out.println(board6Player);
      System.out.println("proteger une position en dehors du plateau");//impossible
      board6Player.setGuard(10, true);
      System.out.println(board6Player);
      System.out.println("hide a gauche en position 5");//impossible
      board6Player.hide(5, true);
      System.out.println(board6Player);
      System.out.println("hide a droite en position 0");//impossible
      board6Player.hide(0, false);
      System.out.println(board6Player);
      System.out.println("hide a gauche en 3");//possible s'il y a un canard en 4 sinon impossible
      board6Player.hide(3, true);
      System.out.println(board6Player);
      System.out.println("hide a droite en 3");//possible s'il y a un canard en 2 sinon impossible
      board6Player.hide(3, false);
      System.out.println(board6Player);
      System.out.println("hide en dehors du plateau");//impossible
      board6Player.hide(10, true);
      System.out.println(board6Player);
      System.out.println("placeDucks alors qu'il y a déjà des canards");//impossible
      board6Player.placeDucks();
      System.out.println(board6Player);
      System.out.println("enlever les canards du plateau");
      board6Player.retrieveDucks();
      System.out.println(board6Player);
      System.out.println("enlever les canards du plateau alors que ça à déjà été fait");
      board6Player.retrieveDucks();
      System.out.println(board6Player);
      System.out.println("placer les canards");
      board6Player.placeDucks();
      System.out.println(board6Player);
      System.out.println("shuffleAll ne mélange que la pile de carte");//ducks doit se modifier et locations doit resté inchangé
      board6Player.shuffleAll();
      System.out.println(board6Player);
      
   }
}


