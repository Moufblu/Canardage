package Protocol;

import javafx.scene.control.Alert;

/**
 * Allow an easy way to show a alert message from a string
 */
public class AlertPopup {
 
   public static void alert(String title, String header, String content, Alert.AlertType alertType) {
      Alert alert = new Alert(alertType);
      alert.setTitle(title);
      alert.setHeaderText(header);
      alert.setContentText(content);

      alert.showAndWait();
   }
}
