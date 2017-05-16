package canardage;

import java.io.Serializable;
import java.util.UUID;


public class Server implements Serializable {
   
   private UUID uuid;
   private String name;
   private String ipAddress;
   private int port;
   
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
   
   public String toString() {
      return "name:" + name + "[" + ipAddress + ":" + port + "]";
   }

   @Override
   public int hashCode() {
      return uuid.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      return uuid.equals(((Server)obj).uuid);
   }
}
