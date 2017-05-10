
package Protocol;

import canardage.Board;

/**
 *
 */
public class ProtocolV1 {
   
   public final static String USE_CARD        = "Action";
   public final static String DISTRIBUTE_HAND = "Hand";
   public final static String REFUSE_CARD     = "Refuse";
   public final static String PATCH_BOARD     = "Board";
   public final static String DISTRIBUTE_CARD = "Card";
   
   public final static String DISCONNECT       = "Stop";
   public final static String YOUR_TURN        = "Turn";
   public final static String ASK_FOR_POSITION = "Position";
   public final static String END_GAME         = "End";
   public final static String SEPARATOR = " ";
   
   public final static String[] ERRORS = {"error1", "error2"};
   
   public final static String ACCEPT_CONNECTION = "Accept";
   public final static String REFUSE_CONNECTION = "Refuse";
   
   public final static int HAND_SIZE    = 3;
   public final static int MIN_ID_CARD  = 0;
   public final static int MAX_ID_CARD  = 4;
   public final static int MIN_NO_POS   = 0;
   public final static int MAX_NO_POS   = 6;

   public final static int PORT       = 1337;
   
   /**
    * Indique quel carte on souhaite jouer
    * @param idCard identifiant de la carte (position dans la tableau des cartes action existantes)
    * @return une string de protocole valide
    */
   public static String messageUseCard(int idCard) {
      String result = USE_CARD + SEPARATOR + idCard;
      return result;
   }
   
   /**
    * Indique le choix de la position
    * @param noPosition numéro de la position choisie
    * @return une string valide du protocol indiquant le choix de la position
    */
   public static String messageAskPosition(int noPosition) {
      String result = ASK_FOR_POSITION + SEPARATOR + noPosition;
      return result;
   }
   
   /**
    * Indique quels cartes sont distribuée au joueur (normalement 3)
    * @param idCards tableau de taille 3 avec les identifiant des cartes
    * @return une String valide du protocol indiquant quels cartes sont distribuées
    */
   public static String messageHand(int[] idCards) {
      String result = DISTRIBUTE_HAND;
      for(int i = 0; i < HAND_SIZE; i++) {
         result += SEPARATOR + idCards[i];
      }
      return result;
   }
   
   /**
    * Permet d'indiquer la distribution d'une carte
    * @param idCard identifiant de la carte distribuée
    * @return une String valide du protocol indiquant la distribution d'une carte
    */
   public static String messageDistributeCard(int idCard) {
      String result = DISTRIBUTE_CARD + SEPARATOR + idCard;
      return result;
   }
   
   /**
    * Indique qu'un choix est refusé par un message d'erreur et son identifiant
    * @param idError position dans le tableau des erreurs connus
    * @return une String valide du protocol indiquant une erreur
    */
   public static String messageRefuse(int idError) {
      String result = REFUSE_CARD + SEPARATOR + idError;
      return result;
   }
   
   /**
    * Permet de récupérer le numéro de l'erreur à partir d'un message de protocol d'erreur valide
    * @param error message du protocol signifiant une erreur
    * @return le numéro de l'erreur soit la position dans le tableau des erreurs connues
    */
   public static int getIdError(String error) {
      String[] splittedError = error.split(SEPARATOR);
      int idError;
      idError = Integer.parseInt(splittedError[1]);
      return idError;
   }
   
   public static String messageBoardState() {
      Board board = Board.getInstance();
      String result = PATCH_BOARD;
      result += SEPARATOR;
      result += board.getBoardState();
      return result;
   }
}
