package canardage;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import canardage.action.*;

/**
 * 
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
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
      
      public void setDuck(int duck) {
         this.duck = duck;
      }
      
      public int removeDuck(){
         int removed = duck;
         duck = hiddenDuck;
         hiddenDuck = MISSING;
         return removed;
      }
   }
   
   private final static int MISSING = -1;
   private final static int WATER_CARD_VALUE = 0;
   public final static int MAX_DUCK_PER_PLAYER = 5;
   public final static int NB_PLAYERS_MIN = 2;
   public final static int NB_PLAYERS_MAX = 6;
   
   private List<Integer> ducks;
   private int currentPlayer = 0;
   
   public final static int NB_LOCATIONS = 6;
   private final Location[] locations;
   
   private static Board instance;

   /**
    * 
    * @param nbPlayers nombre de joueurs accepte
    */
   private Board(int nbPlayers) throws IllegalArgumentException {
      
      if(nbPlayers < NB_PLAYERS_MIN || nbPlayers > NB_PLAYERS_MAX) {
         throw new IllegalArgumentException("Invalid number of players ");
      }
      
      ducks = new LinkedList();
      locations = new Location[NB_LOCATIONS];
      
      for (int i = 0; i < locations.length; i++) {
         locations[i] = new Location();
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
         pushBack(WATER_CARD_VALUE);
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
      shuffle();
      /* place les canards sur la partie visible du plateau */
      placeDucks();
   }
   
   /**
    * Classe Board en tant que Singleton, on utilise la méthode getInstance pour
    * obtenir une instance de la classe Board
    * @param players Nombre de joueur dans la partie
    * @return Une instance de la classe Board
    */
   public static Board getInstance() {
      if (instance != null) {
         return instance;
      } else {
         throw new RuntimeException("Instance not registered !");
      }
   }
   
   /**
    * @brief Instancie le singleton.
    * @param nbPlayers Nombre de joueur dans la partie
    */
   public static void registerInstance(int nbPlayers) {
      if (instance == null)
         instance = new Board(nbPlayers);
   }
   
   /**
    * Méthode pour retourner le nombre maximum de joueurs
    * @return Le nombre maximum de joueurs
    */
   public static int getMaxPlayers() {
      return NB_PLAYERS_MAX;
   }
   
   /**
    * Méthode pour retourner le nombre maximum de joueurs
    * @return Le nombre maximum de joueurs
    */
   public int getNbLocations() {
      return NB_LOCATIONS;
   }
   
   /**
    * @brief Place les 6 premiers canards du deck de canards sur le plateau.
    * 
    * Prend les 6 premiers canards du deck de canards. Chaque canard est placé sur
    * un emplacement du plateau différent.
    */
   private void placeDucks() {
      //nadir
       for (int i = 0; i < NB_LOCATIONS; i++)
       {
           placeDuck(i);
       }
   }
   
   /**
    * @brief Ajoute un canard à la fin du deck de canards
    * @param duck Le canard à ajouter dans le deck
    */
   public void pushBack(int duck) {
      //nadir
       ducks.add(duck);
   }
   
   /**
    * @brief Échange les positions de deux canards adjacents dans le deck
    * @param location L'emplacement de référence d'un canard à intervertir
    * @param left Vrai si on désire intervertir avec le canard adjacent à gauche.
    *             Faux si on désire intervertir avec le canard adjacent à droite.
    * @return 
    * @throws IndexOutOfBoundsException 
    */
   public void swap(int location, boolean left) throws IndexOutOfBoundsException {
      //nadir       
      if (left) {
         if (location < NB_LOCATIONS - 1 && location >= 0) {
            swap(location, location + 1);
         } else {
            throw new IndexOutOfBoundsException("Cannot swap left !");
         }
      } else {
         if (location < NB_LOCATIONS && location > 0) {
            swap(location, location - 1);
         } else {
            throw new IndexOutOfBoundsException("Cannot swap right !");
         }
      }
   }
   
   /**
    * 
    * @param location1
    * @param location2
    * @throws IndexOutOfBoundsException 
    */
   public void swap(int location1, int location2) throws IndexOutOfBoundsException {
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
    * 
    * @param location
    * @param left
    * @return 
    */
   public boolean possibleHide(int location, boolean left) {
      validate(location);
      boolean verify = true;
      int locationWish;
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
      if(locations[location].hiddenDuck != MISSING) {
         verify = false;
      }
      if(locations[locationWish].duck == WATER_CARD_VALUE) {
         verify = false;
      }
      if(locations[location].duck == WATER_CARD_VALUE) {
         verify = false;
      }
      //qu'il y ait des canard sous les positions
      if(locations[location].duck == MISSING) {
         verify = false;
      }
      if(locations[locationWish].duck == MISSING) {
         verify = false;
      }
      return verify;
   }
   
  /**
   * cache un canard derriere un autre
   * @param location position du canard qui souhaite se cacher
   * @param left true si on a choisit de se cacher a gauche
   */
   public void hide(int location, boolean left) throws IndexOutOfBoundsException {
      validate(location);
      
      int locationWish = 0;
      if(left) {
         locationWish = location + 1;
      } else {
         locationWish = location - 1;
      }
      //cache le canard
      if(possibleHide(location, left)) {
         locations[locationWish].hiddenDuck = removeDuck(location);
         advance(location);
      }
      
   }
   
   /**
    * melange la liste ducks mais pas les positions visibles
    */
   private void shuffle(){
      Collections.shuffle(ducks);
   }
   
   public void shuffleAll(){
      retrieveDucks();
      shuffle();
      placeDucks();
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
   
   /**
    * 
    * @param posDuck
    * @param value
    * @throws IndexOutOfBoundsException 
    */
   public void setTarget(int posDuck, boolean value) throws IndexOutOfBoundsException {
      validate(posDuck);
      
      if (!value || locations[posDuck].duck > 0)
      {
         locations[posDuck].targetted = value;
      }
   }
   
   /**
    * 
    * @param location
    * @return
    * @throws IndexOutOfBoundsException 
    */
   public boolean isTargetted(int location) throws IndexOutOfBoundsException
   {
      validate(location);
      return locations[location].targetted;
   }
   
   /**
    * 
    * @param location
    * @param value
    * @throws IndexOutOfBoundsException 
    */
   public void setGuard(int location, boolean value) throws IndexOutOfBoundsException {
      validate(location);
      
      locations[location].guarded = value;
   }
   
   /**
    * 
    * @param location
    * @return
    * @throws IndexOutOfBoundsException 
    */
   public boolean isGuarded(int location) throws IndexOutOfBoundsException {
      validate(location);
      return locations[location].guarded;
   }
   
   /**
    * 
    * @param location
    * @throws IndexOutOfBoundsException 
    */
   public void fire(int location) throws IndexOutOfBoundsException {
      validate(location);
      if(locations[location].targetted){
         removeDuck(location);
         setTarget(location, false);
         advance(location);
      }
   }
   
   /**
    * 
    * @return 
    */
   @Override
   public String toString() {
      String display = "[";
      
      for (int i = locations.length - 1; i >= 0; i--) {
         if (locations[i].targetted) {
            display += "*";
         }
         
         if (locations[i].guarded) {
            display += "G";
         }
         
         display += locations[i].duck;
         
         if (locations[i].hiddenDuck != MISSING) {
            display += "~" + locations[i].hiddenDuck + "~";
         }
         
         if(i != 0){
            display += ", ";
         }
      }
      
      display += "]   [" + ducks.size() + "]";
      
      return display;
   }
   
   /**
    * 
    * @param location
    * @throws IndexOutOfBoundsException 
    */
   private void placeDuck(int location) throws IndexOutOfBoundsException {
      validate(location);
      locations[location].duck = ducks.remove(0);
   }
   
   /**
    * 
    * @param location
    * @return 
    */
   private int removeDuck(int location){
      int duck = locations[location].removeDuck();
      return duck;
   }
   
   /**
    * 
    * @param location 
    */
   private void advance(int location){
      for (int i = location; i != 0; i--) {
         if(locations[i].duck == MISSING) {
            swap(i, false);
         }
         else{
            break;
         }
      }
      
      if(locations[0].duck == MISSING) {
         placeDuck(0);
      }
   }
   
   /**
    * 
    * @param location
    * @throws IndexOutOfBoundsException 
    */
   private void validate(int location) throws IndexOutOfBoundsException
   {
      if (!(location >= 0 && location < NB_LOCATIONS))
      {
         throw new IndexOutOfBoundsException("Location out of bounds !");
      }
   }
   
   /**
    * Méthode qui nous dit s'il y a un canard à une position donnée
    * @return Vrai s'il y a u canard, faux sinon
    */
   public boolean isDuck(int location) throws IndexOutOfBoundsException {
      validate(location);
      return locations[location].duck != 0;
   }
   
   /**
    * Méthode qui nous dit si le canard est le notre ou pas
    * @param location Position de la carte choisie
    * @param player Joueur courrant
    * @return Vrai si c'est le cannard de player, faux sinon
    */
   public boolean isMyDuck(int location, int player) throws IndexOutOfBoundsException {
      validate(location);
      currentPlayer = player;
      if(locations[location].duck == currentPlayer){
         return true;
      }
      return false;
   }
   
   public static void main(String... args) {
      
      //creation de 2 plateau un a 2 joueur et un a 6
      //Board board2Player = new Board(2);
      Board.registerInstance(6);
      Board board6Player = Board.getInstance();
      //instanciation des cartes action
      Target target = new Target();
      Shoot shoot = new Shoot();
      Guard guard = new Guard();
      PlaceBefore placeBefore = new PlaceBefore();
      placeBefore.setPlayer(3);
      Hide hide = new Hide();
      hide.setPlayer(3);
      
      System.out.println("Veuillez entrer uniquement 3 lorsqu'une entrée est demandée pour que tous les tests soient fait");
      
      System.out.println("Etat initial ");
      System.out.println(board6Player);
      
      System.out.println("test avec positions aléatoire :");
      System.out.println("test la carte target");
      target.effect();
      System.out.println(board6Player);
      System.out.println("test la carte shoot");
      shoot.effect();
      System.out.println(board6Player);
      System.out.println("test la carte guard");
      guard.effect();
      System.out.println(board6Player);
      System.out.println("test la carte placeBefore");
      placeBefore.effect();
      System.out.println(board6Player);
      System.out.println("test la carte hide");
      hide.effect();
      System.out.println(board6Player);
      
      /*
      System.out.println("test avec positions définie :");
      System.out.println("test la carte target");
      System.out.println("pas de canard, pas de target on essaye de target -> pas d'effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setTarget(3, false);
      target.effect();
      System.out.println(board6Player);
      System.out.println("un canard, pas de target on essaye de target -> effet");
      board6Player.setLocation(3, 3);
      board6Player.setTarget(3, false);
      target.effect();
      System.out.println(board6Player);
      System.out.println("un canard, une target on essaye de target -> pas d'effet target reste");
      board6Player.setLocation(3, 3);
      board6Player.setTarget(3, true);
      target.effect();
      System.out.println(board6Player);
      System.out.println("pas de canard, une target on essaye de target -> pas d'effet target reste");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setTarget(3, true);
      target.effect();
      System.out.println(board6Player);
      
      System.out.println("");
      System.out.println("");
      
      System.out.println("test la carte shoot");
      System.out.println("pas de canard une target on essaye de shoot -> enleve la cible");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setTarget(3, true);
      shoot.effect();
      System.out.println(board6Player);
      System.out.println("un canard, pas de target on essaye de shoot -> pas d'effet");
      board6Player.setLocation(3, 3);
      board6Player.setTarget(3, false);
      shoot.effect();
      System.out.println(board6Player);
      System.out.println("un canard une target on essaye de shoot -> tue le canard, enleve la cible, decale les cartes restante");
      board6Player.setLocation(3, 3);
      board6Player.setTarget(3, true);
      shoot.effect();
      System.out.println(board6Player);
      System.out.println("pas de canard pas de target on essaye de shoot -> pas d'effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setTarget(3, false);
      shoot.effect();
      System.out.println(board6Player);
      
      System.out.println("");
      System.out.println("");
      
      System.out.println("test la carte guard");
      System.out.println("pas de canard, pas de guard on essaye de guard -> effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setGuard(3, false);
      guard.effect();
      System.out.println(board6Player);
      System.out.println("un canard, pas de guard on essaye de guard -> effet");
      board6Player.setLocation(3, 3);
      board6Player.setGuard(3, false);
      guard.effect();
      System.out.println(board6Player);
      System.out.println("un canard, une guard on essaye de guard -> pas d'effet");
      board6Player.setLocation(3, 3);
      board6Player.setGuard(3, true);
      guard.effect();
      System.out.println(board6Player);
      System.out.println("pas de canard, une guard on essaye de guard -> pas d'effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setGuard(3, true);
      guard.effect();
      System.out.println(board6Player);
      
      System.out.println("");
      System.out.println("");
      
      System.out.println("test la carte placeBefore");
      System.out.println("pas de canard, pas de canardAvant on essaye de placeBefore -> pas d'effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setLocation(2, WATER_CARD_VALUE);
      placeBefore.effect();
      System.out.println(board6Player);
      System.out.println("un canard, pas de canardAvant on essaye de placeBefore -> pas d'effet");
      board6Player.setLocation(3, 3);
      board6Player.setLocation(2, WATER_CARD_VALUE);
      placeBefore.effect();
      System.out.println(board6Player);
      System.out.println("un canard, une canardAvant on essaye de placeBefore -> effet");
      board6Player.setLocation(3, 3);
      board6Player.setLocation(2, 2);
      placeBefore.effect();
      System.out.println(board6Player);
      System.out.println("pas de canard, un canardAvant on essaye de placeBefore -> pas d'effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setLocation(2, 3);
      placeBefore.effect();
      System.out.println(board6Player);
      
      System.out.println("");
      System.out.println("");
      
      
      System.out.println("test la carte hide");
      System.out.println("pas de canard, pas de canardAvant on essaye de hideDroite -> pas d'effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setLocation(2, WATER_CARD_VALUE);
      hide.effect();
      System.out.println(board6Player);
      System.out.println("un canard, pas de canardAvant on essaye de hideDroite -> pas d'effet");
      board6Player.setLocation(3, 3);
      board6Player.setLocation(2, WATER_CARD_VALUE);
      hide.effect();
      System.out.println(board6Player);
      System.out.println("un canard, une canardAvant on essaye de hideDroite -> effet");
      board6Player.setLocation(3, 3);
      board6Player.setLocation(2, 2);
      hide.effect();
      System.out.println(board6Player);
      System.out.println("pas de canard, un canardAvant on essaye de hideDroite -> pas d'effet");
      board6Player.setLocation(3, WATER_CARD_VALUE);
      board6Player.setLocation(2, 3);
      hide.effect();
      System.out.println(board6Player);*/
   }
   
   /**
    * This method exists only to eliminate the random factor in the tests.
    * @param location
    * @param duck 
    */
   public void setLocation(int location, int duck)
   {
      locations[location].setDuck(duck);
   }
}
