package canardage;

public class Global {
   public static class Security {
      public static final String ENCODING_ALGORITHM = "SHA-256";
   }
   
   public static class Text {
      public static final String FORMAT_TEXT = "UTF-8";
   }
   
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
