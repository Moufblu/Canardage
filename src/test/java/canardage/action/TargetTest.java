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

/**
 *
 * @author Nathan
 */
public class TargetTest {
   
   private static Board board;
   
   public TargetTest() {
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
   public void canPutATarget() {
      System.out.println("isPlayable");
      Target t = new Target();
      board.setTarget(0, true);
      board.setTarget(5, true);
      assertTrue(t.isPlayable(3));
   }
   
   @Test
   public void cannotPutATarget() {
      System.out.println("isPlayable");
      Target t = new Target();
      board.setTarget(0, true);
      board.setTarget(5, true);
      assertFalse(t.isPlayable(5));
   }
   
   @Test
   public void cannotPutATargetAtOnePlaceOnBoard() {
      System.out.println("hasEffect");
      Target t = new Target();
      // All targets are set
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
      System.out.println("hasEffect");
      Target t = new Target();
      board.setTarget(0, true);
      board.setTarget(5, true);
      assertTrue(t.hasEffect());
   }
}
