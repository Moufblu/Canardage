package canardage;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Classe pour la partie d'initialisation du serveur de la connexion 
 * client-serveur
 * Date: 03.05.2017
 * @author Nadir Benallal, Nathan Gonzalez Montes, Miguel Pombo Dias, Jimmy Verdasca
 * @version 0.1
 */
public class Server implements Serializable {
   
   private UUID uuid;         // UUID du serveur
   private String name;       // Nom du serveur
   private String ipAddress;  // Adresse IP du serveur
   private int port;          // Port du serveur
   
   /**
    * Constructeur de la classe Server
    * @param uuid UUID du serveur
    * @param name Nom du serveur
    * @param ipAddress Adresse IP du serveur
    * @param port Port du serveur
    */
   public Server(UUID uuid, String name, String ipAddress, int port) {
      this.uuid = uuid;
      this.name = name;
      this.ipAddress = ipAddress;
      this.port = port;
   }

   /**
    * Méthode pour retourner l'adresse IP du serveur
    * @return L'adresse IP du serveur
    */
   public String getIpAddress() {
      return ipAddress;
   }

   /**
    * Méthode pour retourner le nom du serveur
    * @return Le nom du serveur
    */
   public String getName() {
      return name;
   }

   /**
    * Méthode pour retourner le port du serveur
    * @return Le port du serveur
    */
   public int getPort() {
      return port;
   }

   /**
    * Méthode pour retourner l'UUID du serveur
    * @return L'UUID du serveur
    */
   public UUID getUuid() {
      return uuid;
   }

   /**
    * Méthode pour attribuer l'adresse IP du serveur
    * @param ipAddress L'adresser IP donnée au serveur
    */
   public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
   }

   /**
    * Méthode pour attribuer le nom du serveur
    * @param name Le nom donné au serveur
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * Méthode pour attribuer le port du serveur
    * @param port Le port donné au serveur
    */
   public void setPort(int port) {
      this.port = port;
   }

   /**
    * Méthode pour attribuer l'UUID du serveur
    * @param uuid L'UUID donné au serveur
    */
   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }
   
   /**
    * Affichage des attributs du serveur
    * @return La chaîne avec les attributs du serveur
    */
   @Override
   public String toString() {
      return "name:" + name + "[" + ipAddress + ":" + port + "]";
   }
}
