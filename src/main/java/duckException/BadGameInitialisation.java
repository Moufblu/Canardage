package duckException;

public class BadGameInitialisation extends RuntimeException {
   public BadGameInitialisation(String text) {
      super(text);
   }
}
