package canardage;

import canardage.action.Action;
import canardage.action.Guard;
import canardage.action.Hide;
import canardage.action.PlaceBefore;
import canardage.action.Shoot;
import canardage.action.Target;

public class Global {

   public static class Security {

      public static final String ENCODING_ALGORITHM = "SHA-256";
   }

   public static class Text {

      public static final String FORMAT_TEXT = "UTF-8";
   }

   public static class Board {

      public static final char GUARD = 'G';
      public static final char TARGET = '*';
      public static final char SEPARATOR = ' ';
   }

   public static class Rules {

      public final static int MIN_NB_PLAYERS = 3;
      public final static int MAX_NB_PLAYERS = 6;    // Nombre maximum de joueur

      public static final int MAX_ID_CARD = 4; // Maximum de l'id d'une carte
      public static final int MAX_NO_POS = 6; // Maximum pour la position d'une carte
      public static final int MIN_ID_CARD = 0; // Minimum de l'id d'une carte
      public static final int MIN_NO_POS = 0; // Minimum pour la position d'une carte
      public static final int HAND_SIZE = 3; // Totale de carte pour un joueur
   }

   public static Action[] cards = {
      new Guard(),
      new Hide(),
      new PlaceBefore(),
      new Shoot(),
      new Target()
   };

   public enum ERROR_MESSAGES {

      BAD_COMMAND {
         @Override
         public String getMessage() {
            return "Mauvaise commande utilisée";
         }
      },
      UNDEFINED {
         @Override
         public String getMessage() {
            return "erreur inconnue";
         }
      };

      public abstract String getMessage();
   }

}
