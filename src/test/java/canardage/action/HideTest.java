package canardage.action;

import canardage.Board;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HideTest {
   
   private static Board board;
   
   public HideTest() {
   }
   
   @BeforeClass
   public static void setUpClass() {
      Board.registerInstance(Board.getMaxPlayers());
      board = Board.getInstance();
   }
   
   @AfterClass
   public static void tearDownClass() {
   }
   
   @Before
   public void setUp() {
   }
   
   @After
   public void tearDown() {
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
            
      assertFalse(hideCard.isPlayable(3));
   }
   
   /**
    * Test of isPlayable method, of class Hide.
    */
   @Test
   public void ADuckCannotHideIfAdjacentIsAlreadyHidden() {
      System.out.println("isPlayable");
   }
   
   /**
    * Test of isPlayable method, of class Hide.
    */
   @Test
   public void ADuckCanHideIfAdjacentIsADuck() {
      System.out.println("isPlayable");
   }

   /**
    * Test of hasEffect method, of class Hide.
    */
   @Test
   public void testHasEffect() {
      System.out.println("hasEffect");
      Hide instance = new Hide();
      boolean expResult = false;
      boolean result = instance.hasEffect();
      assertEquals(expResult, result);
   }

   /**
    * Test of getDirectionChoice method, of class Hide.
    */
   @Test
   public void testGetDirectionChoice() {
      System.out.println("getDirectionChoice");
      Hide instance = new Hide();
      boolean expResult = false;
      boolean result = instance.getDirectionChoice();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
   
}
