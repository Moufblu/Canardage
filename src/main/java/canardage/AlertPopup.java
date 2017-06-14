package canardage;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Classe pour afficher des popups
 */
public class AlertPopup {

   /**
    * Lance une popup avec des informations
    * @param title   le titre de la fenêtre
    * @param header  La première ligne d'information
    * @param content    le corps du message
    * @param alertType  type d'alerte
    */
   public static void alert(String title, String header, String content, Alert.AlertType alertType) {

      Platform.runLater(() -> {
         Alert alert = new Alert(alertType);
         alert.setTitle(title);
         alert.setHeaderText(header);
         alert.setContentText(content);

         alert.showAndWait();
      });
   }

   /**
    * Affiche une exception avec une popup
    * @param e l'exception
    */
   public static void alert(Exception e) {
      String error = "";

      for(StackTraceElement stackTrace1 : e.getStackTrace()) {
         error += stackTrace1 + "\n";
      }

      AlertPopup.alert("Fatal Error", e.getMessage(), error, Alert.AlertType.ERROR);
   }
}
