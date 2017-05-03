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
   
   private Board board;
   
   @BeforeClass
   public static void setUpClass() {
      Board.registerInstance(Board.getMaxPlayers());
   }
   
   @Before
   public void setup() {
      board = Board.getInstance();
      
      for (int i = 0; i < board.getNbLocations(); i++)
      {
         board.setTarget(i, false);
         board.setLocation(i, 1);
      }
   }
   
   @Test
   public void canPutATarget() {
      System.out.println("isPlayable");
      Target t = new Target();
      board.setTarget(3, false);
      assertTrue(t.isPlayable(3));
   }
   
   @Test
   public void cannotPutATarget() {
      System.out.println("isPlayable");
      Target t = new Target();
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
      board.setTarget(0, false);
      assertTrue(t.hasEffect());
   }
}
