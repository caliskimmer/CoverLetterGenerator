import javax.swing.*;


public class GUI extends JFrame{
    private Form form;
    private static GUI instance;
    private static final String APP_NAME = "Cover Letter Generator";

    private GUI(String name) {
        this.setName(name);
        this.form = new Form();
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
        this.setUp();
        this.pack();
        this.setVisible(true);
    }

    private void setUp() {
        form.build();
        getInstance().add(form);
    }
}
