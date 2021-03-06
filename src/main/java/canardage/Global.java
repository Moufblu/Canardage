package canardage;

import canardage.action.Action;
import canardage.action.Guard;
import canardage.action.Hide;
import canardage.action.PlaceBefore;
import canardage.action.Shoot;
import canardage.action.Target;
import chat.Emoticon;

public class Global {

   public static class Security {

      public static final String ENCODING_ALGORITHM = "SHA-256";
   }

   public static class Text {

      public static final String FORMAT_TEXT = "UTF-8";
   }

   public static class BoardParam {

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

   /**
    * Description: Protocol pour la connexion client-serveur
    * Date: 03.05.2017
    * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
    * @version 0.1
    */
   public static class ProtocolV1 {

      // Adresse multicast pour annoncer le serveur
      public static final String MULTICAST_ADDRESS = "229.30.30.30";
      public static final int MULTICAST_PORT = 5001;
      public static final int CHAT_PORT = 5002;
      public static final String EMOTICON = "Emote";
      public static final String USE_CARD = "Action"; // Utilisation d'une carte
      public static final String DISTRIBUTE_HAND = "Hand"; // Donner les cartes
      public static final String REFUSE_CARD = "Refuse"; // Refuser une action
      public static final String PATCH_BOARD = "Board"; // Affichage du plateau
      public static final String DISTRIBUTE_CARD = "Card"; // Donner une carte
      public static final String DISCONNECT = "Stop"; // Déconnecter
      public static final String YOUR_TURN = "Turn"; // Donner le tour
      public static final String ASK_FOR_POSITION = "Position"; // Position sur plateau
      public static final String END_GAME = "End"; // Fin de la partie
      public static final String SEPARATOR = " "; // Séparateur
      public static final String[] ERRORS = {"Mauvaise commande utilisee", "Erreur pas definie"}; //Possibles erreurs
      public static final String ACCEPT_CONNECTION = "Accept"; // Accepter un requête
      public static final String REFUSE_CONNECTION = "Refuse"; // Refuser un requête
      public static final String HASH = "Hash";
      public static final int HASH_SIZE = 64;
      public static final int PORT = 1337; // Port de connexion sur le serveur

      public static String messageAccept(int playerNumber) throws IllegalArgumentException {
         if(playerNumber >= Global.Rules.MAX_NO_POS || playerNumber < Global.Rules.MIN_NO_POS) {
            throw new IllegalArgumentException("Joueur invalide: " + playerNumber);
         }
         String result = ACCEPT_CONNECTION + SEPARATOR + playerNumber;
         return result;
      }

      /**
       * Indique quel carte on souhaite jouer
       * @param idCard identifiant de la carte (position dans la tableau des cartes
       * action existantes)
       * @return une string de protocole valide
       * @throws IllegalArgumentException si l'id de la carte est inexistante
       */
      public static String messageUseCard(int idCard) throws IllegalArgumentException {
         if(idCard < Global.Rules.MIN_ID_CARD || idCard > Global.Rules.MAX_ID_CARD) {
            throw new IllegalArgumentException("Id carte invalide: " + idCard);
         }
         String result = USE_CARD + SEPARATOR + idCard;
         return result;
      }

      /**
       * Indique le choix de la position
       * @param noPosition numéro de la position choisie
       * @return une string valide du protocol indiquant le choix de la position
       * @throws IllegalArgumentException si le numéro de la position est impossible
       */
      public static String messageAskPosition(int noPosition) throws IllegalArgumentException {
         if(noPosition < Global.Rules.MIN_NO_POS || noPosition > Global.Rules.MAX_NO_POS) {
            throw new IllegalArgumentException("Num\u00e9ro de case impossible: " + noPosition);
         }
         String result = ASK_FOR_POSITION + SEPARATOR + noPosition;
         return result;
      }

      /**
       * Indique quels cartes sont distribuée au joueur (normalement 3)
       * @param idCards tableau de taille 3 avec les identifiant des cartes
       * @return une String valide du protocol indiquant quels cartes sont distribuées
       * @throws IllegalArgumentException si le nombre de cartes est faut ou si une
       * carte ne possède pas un indientifiant valide
       */
      public static String messageHand(Integer[] idCards) throws IllegalArgumentException {
         // check si la taille du tableau est bien celle d'une main
         if(idCards.length != Global.Rules.HAND_SIZE) {
            throw new IllegalArgumentException("Nombres de cartes de la main invalide: " + idCards.length);
         }
         // check si toutes les cartes passées ont un id valdie
         for(int i = 0; i < Global.Rules.HAND_SIZE; i++) {
            if(idCards[i] < Global.Rules.MIN_ID_CARD || idCards[i] > Global.Rules.MAX_ID_CARD) {
               throw new IllegalArgumentException("Id carte invalide: " + idCards[i]);
            }
         }
         String result = DISTRIBUTE_HAND;
         for(int i = 0; i < Global.Rules.HAND_SIZE; i++) {
            result += SEPARATOR + idCards[i];
         }
         return result;
      }

      public static String messageHash(String hash) throws IllegalArgumentException {
         //check si la taille du tableau est bien celle d'une main
         if(hash.length() != HASH_SIZE) {
            throw new IllegalArgumentException("hash invalide size: " + hash.length());
         }
         String result = HASH + SEPARATOR + hash;
         return result;
      }

      /**
       * Permet d'indiquer la distribution d'une carte
       * @param idCard identifiant de la carte distribuée
       * @return une String valide du protocol indiquant la distribution d'une carte
       * @throws IllegalArgumentException si l'identifiant de la carte est impossible
       */
      public static String messageDistributeCard(int idCard) throws IllegalArgumentException {
         if(idCard < Global.Rules.MIN_ID_CARD || idCard > Global.Rules.MAX_ID_CARD) {
            throw new IllegalArgumentException("Id carte invalide: " + idCard);
         }
         String result = DISTRIBUTE_CARD + SEPARATOR + idCard;
         return result;
      }

      /**
       * Indique qu'un choix est refusé par un message d'erreur et son identifiant
       * @param message message à envoyer
       * @return une String valide du protocol indiquant une erreur
       * @throws IllegalArgumentException si l'erreur n'existe pas dans le tableau des
       * erreurs connues
       */
      public static String messageError(ERROR_MESSAGES message) {
         return REFUSE_CARD + SEPARATOR + message;
      }

      /**
       * Permet de récupérer le numéro de l'erreur à partir d'un message de protocol
       * d'erreur valide
       * @param error message du protocol signifiant une erreur
       * @return le numéro de l'erreur soit la position dans le tableau des erreurs
       * connues
       * @throws IllegalArgumentException soit parce que le numéro de l'erreur est
       * invalide, soit parce que le message d'erreur n'est pas au format du protocol
       */
      public static int getIdError(String error) throws IllegalArgumentException {
         String[] splittedError = error.split(SEPARATOR);
         int idError;
         try {
            idError = Integer.parseInt(splittedError[1]);
            if(idError < 0 || idError >= ERRORS.length) {
               throw new IllegalArgumentException("Id d'erreur n'existant pas: " + idError);
            }
         } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Cette erreur n'est pas enregistree : " + error);
         }
         return idError;
      }

      /**
       * Affichage de l'état du plateau
       * @return Le message à afficher
       */
      public static String messageBoardState() {
         Board board = Board.getInstance();
         String result = PATCH_BOARD;
         result += SEPARATOR;
         result += board.getBoardState();
         return result;
      }

      public static String messageChat(int player, Emoticon emote) {
         if(player < Global.Rules.MIN_NO_POS || player >= Global.Rules.MAX_NB_PLAYERS) {
            throw new IllegalArgumentException("Joueur invalide: " + player);
         }
         String result = EMOTICON + SEPARATOR + player + SEPARATOR + emote;
         return result;
      }
   }

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
