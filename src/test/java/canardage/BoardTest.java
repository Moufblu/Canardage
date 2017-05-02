/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage;

import java.util.Stack;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel-Portable
 */
public class BoardTest {
   private Board board;
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
      board = new Board();
   }
   
   @After
   public void tearDown() {
   }
   @Test
   public void boardPopDeleteTheFirstElementAndAddItAtTheEnd {
      board.add(1);
      board.add(2);
      board.add(3);
      assertEquals(1, board.pop()); //retourne et enlève le premier élément du board
      assertEquals(2, board.pop());
      assertEquals(3, board.pop());
      assertEquals(1, board.pop());
   }
           
   @Test
   public void pileCanAddElementAtTheQueue {
      board.add(1);
      board.add(2);
      board.pop();
      assertEquals(2, board.pop());
   }
   
   @Test
   public void pileHasCorrectSize {
      board.add(1);
      board.add(2);
      board.add(3);
      int expectedSize = 3;
      assertEquals( expectedSize, board.size());
   }
   
   @Test
   public void pileCanSwap6FirstElements {
      board.add(1);
      board.add(2);
      board.add(3);
      board.add(4);
      board.add(5);
      board.add(6);
      board.swap(4, 5);
      board.swap(0, 4);
      assertEquals()
   }
   
   @Test
   public void pileCantSwapElementsAfter6th {
   
   }
   
}
