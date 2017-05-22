/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import canardage.Board;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GuardTest {

   private Board board;
   private Guard guard;

   public GuardTest() {
   }

   @BeforeClass
   public static void setUpClass() {
      Board.registerInstance(Board.getMaxPlayers());
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {
      board = Board.getInstance();
      guard = new Guard();

      for(int i = 0; i < board.getNbLocations(); i++) {
         board.setGuard(i, false);
      }
   }

   @After
   public void tearDown() {
   }

   /**
    * Test of isPlayable method, of class Guard.
    */
   @Test
   public void CannotBePlayedIfIsGuarded() {
      System.out.println("isPlayable");
      board.setGuard(0, true);
      assertFalse(guard.isPlayable(0));
   }

   /**
    * Test of isPlayable method, of class Guard.
    */
   @Test
   public void CanBePlayedIfThereIsADuck() {
      System.out.println("isPlayable");
      board.setLocation(0, 3);
      board.setGuard(0, false);
      assertTrue(guard.isPlayable(0));
   }

   /**
    * Test of isPlayable method, of class Guard.
    */
   @Test
   public void CanBePlayedEvenIfNotADuck() {
      System.out.println("isPlayable");
      board.setLocation(0, 0);
      board.setGuard(0, false);
      assertTrue(guard.isPlayable(0));
   }

   /**
    * Test of isPlayable method, of class Guard.
    */
   @Test
   public void HasEffectIfALocationIsNotGuarded() {
      System.out.println("isPlayable");
      board.setGuard(0, false);
      assertTrue(guard.hasEffect());
   }

   /**
    * Test of isPlayable method, of class Guard.
    */
   @Test
   public void HasNoEffectIfEveryLocationIsProtected() {
      System.out.println("isPlayable");
      board.setGuard(0, true);
      board.setGuard(1, true);
      board.setGuard(2, true);
      board.setGuard(3, true);
      board.setGuard(4, true);
      board.setGuard(5, true);
      assertFalse(guard.hasEffect());
   }
}
