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

   public String getIpAddress() {
      return ipAddress;
   }

   public String getName() {
      return name;
   }

   public int getPort() {
      return port;
   }

   public UUID getUuid() {
      return uuid;
   }

   public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPort(int port) {
      this.port = port;
   }

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
