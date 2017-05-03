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
public class ShootTest {
   
   private static Board board;
   
   public ShootTest() {
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
   public void cannotShootIfNotTargeted() {
      System.out.println("isPlayable");
      Shoot sh = new Shoot();
      board.setTarget(0, true);
      board.setTarget(5, true);
      assertFalse(sh.isPlayable(3));
   }
   
   @Test
   public void canShootIfTargeted() {
      System.out.println("isPlayable");
      Shoot sh = new Shoot();
      board.setTarget(0, true);
      board.setTarget(5, true);
      assertTrue(sh.isPlayable(0));
   }
   
   @Test
   public void canShootAtOnePlaceOnBoard() {
      System.out.println("hasEffect");
      Shoot sh = new Shoot();
      board.setTarget(1, true);
      board.setTarget(3, true);
      board.setTarget(5, true);
      assertTrue(sh.hasEffect());
   }
   
   @Test
   public void cannotShootAtOnePlaceOnBoard() {
      System.out.println("hasEffect");
      Shoot sh = new Shoot();
      // There aren't targets
      assertFalse(sh.hasEffect());
   }
}
