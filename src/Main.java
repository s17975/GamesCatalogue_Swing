import javax.swing.*;

/**
 * @author Marcin Rudko
 * @version 31.05.2018
 **/

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Model model = new Model();
                GUI gui = new GUI();
                Controler controler = new Controler(gui, model);
            }
        });

    }
}
