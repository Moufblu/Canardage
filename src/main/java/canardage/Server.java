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
   
   public String toString() {
      return "name:" + name + "[" + ipAddress + ":" + port + "]";
   }
   
}
