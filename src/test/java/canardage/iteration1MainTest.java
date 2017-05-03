/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage;

/**
 *
 * @author jimmy
 */
public class iteration1MainTest {
   /*public static void main(String... args) {
      
      //creation de 2 plateau un a 2 joueur et un a 6
      //Board board2Player = new Board(2);
      Board.registerInstance(6);
      Board board6Player = Board.getInstance();
      
      //System.out.println("plateau pour 2 joueurs :");
      //System.out.println(board2Player);
      //System.out.println("");
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
      if (board6Player.isTargetted(3))
      {
         System.out.println("Deja cible !!!");
      }
      else
      {
         board6Player.setTarget(3, true);
      }
      System.out.println(board6Player);
      System.out.println("placer une cible en dehors du plateau");
      try {
         board6Player.setTarget(10, true);
      } catch(IndexOutOfBoundsException e) {
         System.out.println(e.toString());
      }
      System.out.println(board6Player);
      System.out.println("tirer alors qu'il y a une cible");//canard meurt et cible disparait /eau cible disparait
      board6Player.fire(3);
      System.out.println(board6Player);
      System.out.println("tirer alors qu'il n'y a pas de cible");//impossible
      board6Player.fire(4);
      System.out.println(board6Player);
      System.out.println("tirer en dehors du plateau");
      try {
         board6Player.fire(10);
      } catch(IndexOutOfBoundsException e) {
         System.out.println(e.toString());
      }
      System.out.println(board6Player);
      System.out.println("proteger une position");
      board6Player.setGuard(3, true);
      System.out.println(board6Player);
      System.out.println("proteger une position déjà protégé"); //impossible
      if (board6Player.isGuarded(3))
      {
         System.out.println("Deja garde !!!");
      }
      else
      {
         board6Player.setGuard(3, true);
      }
      System.out.println(board6Player);
      System.out.println("tirer en 3 sur une position protégé");
      board6Player.fire(3);
      System.out.println(board6Player);
      System.out.println("enlever une protection");
      board6Player.setGuard(3, false);
      System.out.println(board6Player);
      System.out.println("proteger une position en dehors du plateau");//impossible
      try {
         board6Player.setGuard(10, true);
      } catch(IndexOutOfBoundsException e) {
         System.out.println(e.toString());
      }
      System.out.println(board6Player);
      System.out.println("hide a gauche en position 5");//impossible
      try {
         if(board6Player.possibleHide(5, true)) {
            board6Player.hide(5, true);
         }
         else {
            System.out.println("pas pu deplacer");
         }
      } catch(IndexOutOfBoundsException e) {
         System.out.println(e.toString());
      }
      System.out.println(board6Player);
      System.out.println("hide a droite en position 0");//impossible
      try {
         if(board6Player.possibleHide(0, false)) {
            board6Player.hide(0, false);
         }
         else {
            System.out.println("pas pu deplacer");
         }
      } catch(IndexOutOfBoundsException e) {
         System.out.println(e.toString());
      }
      System.out.println(board6Player);
      System.out.println("hide a gauche en 3");//possible s'il y a un canard en 4 sinon impossible
      if(board6Player.possibleHide(3, true)) {
         board6Player.hide(3, true);
      }
      else {
         System.out.println("pas pu deplacer");
      }
      System.out.println(board6Player);
      System.out.println("hide a droite en 3");//possible s'il y a un canard en 2 sinon impossible
      if(board6Player.possibleHide(3, false)) {
         board6Player.hide(3, false);
      }
      else {
         System.out.println("pas pu deplacer");
      }
      System.out.println(board6Player);
      System.out.println("hide en dehors du plateau");//impossible
      try {
         if(board6Player.possibleHide(10, true)) {
            board6Player.hide(10, true);
         }
         else {
            System.out.println("pas pu deplacer");
         }
      } catch(IndexOutOfBoundsException e) {
         System.out.println(e.toString());
      }
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
      System.out.println("shuffle ne mélange que la pile de carte");//ducks doit se modifier 
      board6Player.shuffle();
      System.out.println(board6Player);
      System.out.println("shuffleAll mélange tout");//ducks doit se modifier 
      board6Player.shuffleAll();
      System.out.println(board6Player);
   }*/
}
