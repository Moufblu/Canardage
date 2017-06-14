package duckException;

/**
 * Description: Classe pour signaler une erreur
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class BadGameInitialisation extends RuntimeException {

   /**
    * Constructeur de la classe BadGameInitialisation
    * @param text Teste à afficher
    */
   public BadGameInitialisation(String text) {
      super(text);
   }
}
