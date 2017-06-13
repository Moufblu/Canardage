/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canardage.action;

import canardage.Board;
import canardage.Client;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 * On peut pas encore faire ces tests sans un joueur courrant
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 */
@Ignore
public class PlaceBeforeTest {

   private static Board board;

   @BeforeClass
   public static void setUpClass() {
      Board.registerInstance(Board.getMaxPlayers());
   }

   @Before
   public void setUp() {
      board = Board.getInstance();

      board.setTarget(0, false);
      board.setTarget(1, false);
      board.setTarget(2, false);
      board.setTarget(3, false);
      board.setTarget(4, false);
      board.setTarget(5, false);
   }

   /**
    * Test de isPlayable, de la classe PlaceBefore.
    */
   @Test
   public void canPlaceYourDuckOnePlaceBeforeWhereItIs() {
      System.out.println("isPlayable");
      PlaceBefore pb = new PlaceBefore();
      Client client = null;
      try {
         client = new Client(null);
      } catch(IOException ex) {
         Logger.getLogger(PlaceBeforeTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      client.setId(1);
      Action a = new Action() {
         @Override
         public void effect() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }
         

         @Override
         public boolean hasEffect() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }

         @Override
         public int getId() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }

         @Override
         public int getNbCards() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }
      };
      a.setPlayer(client);
      board.setLocation(3, 1);
      board.setLocation(2, 0);
      assertTrue(pb.isPlayable(3));
   }

   /**
    * Test de hasEffect, de la classe PlaceBefore.
    */
   @Test
   public void canPlaceOneOfYourDuckOnePlaceBeforeWhereItIs() {
      System.out.println("hasEffect");
      PlaceBefore instance = new PlaceBefore();
      boolean expResult = false;
      boolean result = instance.hasEffect();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

}
