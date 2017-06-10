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
   
   public static Action[] cards = {
      new Guard(),
      new Hide(),
      new PlaceBefore(),
      new Shoot(),
      new Target()
   };

   public enum ERROR_MESSAGES{
      
      BAD_COMMAND{
         @Override
         public String getMessage() {
            return "Mauvaise commande utilisée";
         }
      },
      UNDEFINED{
         @Override
         public String getMessage() {
            return "erreur inconnue";
         }
      };
      
      public abstract String getMessage();
   }
}
