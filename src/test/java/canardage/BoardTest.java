package canardage;

import canardage.action.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
public class BoardTest {
   
   private static Board board;
   
   public BoardTest() {
   }
   
   @BeforeClass
   public static void setUpClass() {
   }
   
   @AfterClass
   public static void tearDownClass() {
   }
   
   @Before
   public void setUp() {
      Board.registerInstance(Board.getMaxPlayers());
      board = Board.getInstance();
   }
   
   @After
   public void tearDown() {
   }
   @Test
   public void boardPopDeleteTheFirstElementAndAddItAtTheEnd() {
      board.pushBack(1);
      board.pushBack(2);
      board.pushBack(3);
      assertEquals(1, board.pop()); //retourne et enlève le premier élément du board
      assertEquals(2, board.pop());
      assertEquals(3, board.pop());
      assertEquals(1, board.pop());
   }
           
   @Test
   public void pileCanAddElementAtTheQueue() {
      board.pushBack(1);
      board.pushBack(2);
      board.pop();
      assertEquals(2, board.pop());
   }
   
   @Test
   public void pileHasCorrectSize() {
      board.pushBack(1);
      board.pushBack(2);
      board.pushBack(3);
      int expectedSize = 3;
      assertEquals(expectedSize, board.size());
   }
   
   @Test
   public void pileCanSwap6FirstElements() {
      board.pushBack(1);
      board.pushBack(2);
      board.pushBack(3);
      board.pushBack(4);
      board.pushBack(5);
      board.pushBack(6);
      board.swap(4, 5);
      board.swap(0, 4);
      assertEquals();
   }
   
   @Test
   public void pileCantSwapElementsAfter6th() {
      
   }
}
