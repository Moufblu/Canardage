package Protocol;

import canardage.Board;
import chat.Emoticon;

/**
 * Description: Protocol pour la connexion client-serveur
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class ProtocolV1 {
   
   // Adresse multicast pour annoncer le serveur
   public final static String MULTICAST_ADDRESS = "229.30.30.30";
   public final static int MULTICAST_PORT     = 5001;
   public final static int CHAT_PORT          = 5002;
   
   public final static String EMOTICON        = "Emote";
   
   public final static String USE_CARD        = "Action";  // Utilisation d'une carte
   public final static String DISTRIBUTE_HAND = "Hand";    // Donner les cartes
   public final static String REFUSE_CARD     = "Refuse";  // Refuser une action
   public final static String PATCH_BOARD     = "Board";   // Affichage du plateau
   public final static String DISTRIBUTE_CARD = "Card";    // Donner une carte
   
   public final static String DISCONNECT       = "Stop";    // Déconnecter
   public final static String YOUR_TURN        = "Turn";    // Donner le tour
   public final static String ASK_FOR_POSITION = "Position";// Position sur plateau
   public final static String END_GAME         = "End";     // Fin de la partie
   public final static String SEPARATOR        = " ";       // Séparateur
   
   public final static String[] ERRORS = {"Mauvaise commande utilisee",
                                         "Erreur pas definie"};   //Possibles erreurs
   
   public final static String ACCEPT_CONNECTION = "Accept"; // Accepter un requête
   public final static String REFUSE_CONNECTION = "Refuse"; // Refuser un requête
   
   public final static String HASH = "Hash";
   public final static int HASH_SIZE = 64;
   
   public final static int HAND_SIZE    = 3; // Totale de carte pour un joueur
   public final static int MIN_ID_CARD  = 0; // Minimum de l'id d'une carte
   public final static int MAX_ID_CARD  = 4; // Maximum de l'id d'une carte
   public final static int MIN_NO_POS   = 0; // Minimum pour la position d'une carte
   public final static int MAX_NO_POS   = 6; // Maximum pour la position d'une carte

   public final static int PORT      = 1337; // Port de connexion sur le serveur
   
   public static String messageAccept(int playerNumber)
                           throws IllegalArgumentException {
      if(playerNumber < MAX_NO_POS || playerNumber > MIN_NO_POS) {
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
   public static String messageUseCard(int idCard) throws IllegalArgumentException{
      if(idCard < MIN_ID_CARD || idCard > MAX_ID_CARD) {
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
   public static String messageAskPosition(int noPosition)
                           throws IllegalArgumentException {
      if(noPosition < MIN_NO_POS || noPosition > MAX_NO_POS) {
         throw new IllegalArgumentException("Numéro de case impossible: " + noPosition);
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
   public static String messageHand(int[] idCards) throws IllegalArgumentException {
      // check si la taille du tableau est bien celle d'une main
      if (idCards.length != HAND_SIZE) {
         throw new IllegalArgumentException("Nombres de cartes de la main invalide: "
                                            + idCards.length);
      }
      // check si toutes les cartes passées ont un id valdie
      for (int i = 0; i < HAND_SIZE; i++) {
         if(idCards[i] < MIN_ID_CARD || idCards[i] > MAX_ID_CARD) {
            throw new IllegalArgumentException("Id carte invalide: " + idCards[i]);
         }
      }
      
      String result = DISTRIBUTE_HAND;
      for(int i = 0; i < HAND_SIZE; i++) {
         result += SEPARATOR + idCards[i];
      }
      return result;
   }
   
   public static String messageHash(String hash) throws IllegalArgumentException{
      //check si la taille du tableau est bien celle d'une main
      if (hash.length() != HASH_SIZE) {
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
   public static String messageDistributeCard(int idCard)
                           throws IllegalArgumentException {
      if(idCard < MIN_ID_CARD || idCard > MAX_ID_CARD) {
         throw new IllegalArgumentException("Id carte invalide: " + idCard);
      }
      
      String result = DISTRIBUTE_CARD + SEPARATOR + idCard;
      return result;
   }
   
   /**
    * Indique qu'un choix est refusé par un message d'erreur et son identifiant
    * @param idError position dans le tableau des erreurs connus
    * @return une String valide du protocol indiquant une erreur
    * @throws IllegalArgumentException si l'erreur n'existe pas dans le tableau des 
    * erreurs connues
    */
   public static String messageRefuse(int idError) throws IllegalArgumentException {
      if(idError < 0 || idError >= ERRORS.length) {
         throw new IllegalArgumentException("Id d'erreur n'existant pas: " + idError);
      }
      
      String result = REFUSE_CARD + SEPARATOR + idError;
      return result;
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
            throw new IllegalArgumentException("Id d'erreur n'existant pas: "
                                               + idError);
         }
      }
      catch (NumberFormatException e) {
         throw new IllegalArgumentException("Cette erreur n'est pas enregistree : "
                                            + error);
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
      if(player < MAX_NO_POS || player > MIN_NO_POS) {
         throw new IllegalArgumentException("Joueur invalide: " + player);
      }
      
      String result = EMOTICON + SEPARATOR + player + SEPARATOR + emote;
      return result;
   }
}
