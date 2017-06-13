package Protocol;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Allow an easy way to show a alert message from a string
 */
public class AlertPopup {

   public static void alert(String title, String header, String content, Alert.AlertType alertType) {

      Platform.runLater(() -> {
         Alert alert = new Alert(alertType);
         alert.setTitle(title);
         alert.setHeaderText(header);
         alert.setContentText(content);

         alert.showAndWait();
      });
   }

   public static void alert(Exception e) {
      String error = "";

      for(StackTraceElement stackTrace1 : e.getStackTrace()) {
         error += stackTrace1 + "\n";
      }

      AlertPopup.alert("Fatal Error", e.getMessage(), error, Alert.AlertType.ERROR);
   }
}
