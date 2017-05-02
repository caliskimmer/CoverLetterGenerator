import javax.swing.*;
import java.io.File;


public class GUI extends JFrame{
    private FormController controller;
    private static GUI instance;
    private static final String APP_NAME = "Cover Letter Generator";

    private GUI(String name) {
        this.setName(name);
        this.controller = new FormController();
    }

    public static GUI getInstance() {
        if (instance == null) {
            instance = new GUI(APP_NAME);
        }

        return instance;
    }

    public void run() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setResizable(false);
        this.setUp();
        this.pack();
        this.setVisible(true);
    }

    private void setUp() {
        Form form = controller.buildFormView();
        getInstance().add(form);
    }
}
