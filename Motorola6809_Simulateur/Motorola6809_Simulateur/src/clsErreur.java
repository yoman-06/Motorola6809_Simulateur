
import javax.swing.JOptionPane;

public class clsErreur extends JOptionPane {




    public static void afficherMessage() {
        showMessageDialog(
            null,
            "votre code contient des erreurs!\n",
            "Erreur",
            JOptionPane.ERROR_MESSAGE
        );
        clsMoto6809.Reset();
    }
    public static void afficherMessage(String message) {
        showMessageDialog(
            null,
            message,
            "Attention",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}