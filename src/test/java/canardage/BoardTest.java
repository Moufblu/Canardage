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
   private int players;
   
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
   
   // Shoot Target
   
   @Test
   public void cannotShootIfNotTargeted() {
      Shoot sh = new Shoot();
      board.setTarget(5, true);
      board.setTarget(0, true);
      assertFalse(sh.isPlayable(3));
   }
   
   @Test
   public void canShootIfTargeted() {
      Shoot sh = new Shoot();
      board.setTarget(5, true);
      board.setTarget(0, true);
      assertTrue(sh.isPlayable(0));
   }
   
   @Test
   public void canPutATarget() {
      Target t = new Target();
      board.setTarget(5, true);
      board.setTarget(0, true);
      assertTrue(t.isPlayable(3));
   }
   
   @Test
   public void cannotPutATarget() {
      Target t = new Target();
      board.setTarget(5, true);
      board.setTarget(0, true);
      assertFalse(t.isPlayable(5));
   }
   
   @Test
   public void cannotPutATargetAtOnePlaceOnBoard() {
      Target t = new Target();
      board.setTarget(0, true);
      board.setTarget(1, true);
      board.setTarget(2, true);
      board.setTarget(3, true);
      board.setTarget(4, true);
      board.setTarget(5, true);
      assertFalse(t.hasEffect());
   }
   
   @Test
   public void canPutATargetAtOnePlaceOnBoard() {
      Target t = new Target();
      board.setTarget(5, true);
      board.setTarget(0, true);
      assertTrue(t.hasEffect());
   }
   
   @Test
   public void canShootAtOnePlaceOnBoard() {
      Shoot sh = new Shoot();
      board.setTarget(1, true);
      board.setTarget(3, true);
      board.setTarget(5, true);
      assertTrue(sh.hasEffect());
   }
   
   @Test
   public void cannotShootAtOnePlaceOnBoard() {
      Shoot sh = new Shoot();
      assertFalse(sh.hasEffect());
   }
}
