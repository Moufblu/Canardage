package canardage.action;

import canardage.Board;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class HideTest {
   
   private Board board;
   private Hide hide;
   
   @BeforeClass
   public static void setUpClass() {
      Board.registerInstance(Board.getMaxPlayers());
   }
   
   @Before
   public void setUp() {
      board = Board.getInstance();
      hide = new Hide();
      
      for (int i = 0; i < board.getNbLocations(); i++)
      {
         board.setLocation(i, 1);
         board.setTarget(i, false);
         board.setGuard(i, false);
      }
   }

   /**
    * Test of isPlayable method, of class Hide.
    */
   @Test
   public void ADuckCannotHideIfAdjacentIsNotADuck() {
      System.out.println("isPlayable");
      board.setLocation(2, 0);
      board.setLocation(3, 3);
      board.setLocation(4, 0);
            
      assertFalse(hide.isPlayable(3));
   }
   
   /**
    * Test of isPlayable method, of class Hide.
    */
   @Test
   public void ADuckCannotHideIfAdjacentIsAlreadyHidden() {
      System.out.println("isPlayable");
      board.setLocation(0, 2);
      board.setLocation(1, 2);
      board.setLocation(2, 2);
      board.setLocation(3, 2);
      board.setLocation(4, 2);
      board.setLocation(5, 2);
      
      board.hide(1, true);
      board.hide(2, false);
      
      assertFalse(hide.isPlayable(1));
   }
   
   /**
    * Test of isPlayable method, of class Hide.
    */
   @Ignore
   public void ADuckCanHideIfAdjacentIsADuck() {
      System.out.println("isPlayable");
      
      board.setLocation(0, 2);
      board.setLocation(1, 2);
      board.setLocation(2, 2);
      
      assertTrue(hide.isPlayable(1));
   }

   /**
    * Test of isPlayable method, of class Hide.
    */
   @Test
   public void ADuckCannotHideIfDuckBehindHim() {
      System.out.println("isPlayable");
      
      board.setLocation(0, 2);
      board.setLocation(1, 2);
      board.hide(0, true);
      
      assertFalse(hide.isPlayable(0));
   }
   
   /**
    * Test of hasEffect method, of class Hide.
    */
   @Ignore
   public void testHasEffectIfDucksInPlay() {
      System.out.println("hasEffect");
      board.setLocation(0, 2);
      board.setLocation(1, 2);
      board.setLocation(2, 2);
      board.setLocation(3, 2);
      board.setLocation(4, 2);
      board.setLocation(5, 2);
      
      assertTrue(hide.hasEffect());
   }
   
   /**
    * Test of hasEffect method, of class Hide.
    */
   @Test
   public void testHasNoEffectIfAnyDuckHasNoAdjacent() {
      System.out.println("hasEffect");
      board.setLocation(0, 2);
      board.setLocation(1, 0);
      board.setLocation(2, 2);
      board.setLocation(3, 0);
      board.setLocation(4, 2);
      board.setLocation(5, 0);
      
      assertFalse(hide.hasEffect());
   }
   
   @Ignore
   public void testHasNoEffectIfAllPlacesHaveAlreadyAHiddenDuck() {
      System.out.println("hasEffect");
      for(int i = 0; i < Board.NB_LOCATIONS - 1; i++){
         board.setLocation(i, 2);
         board.setLocation(i + 1, 2);
         board.hide(i, true);
      }
      
      assertFalse(hide.hasEffect());
   }

}
