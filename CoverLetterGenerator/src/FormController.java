import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

//for binding GUI with generator logic

public class FormController {
    private Form form;
    private CLGenerator generator;

    public FormController() {
        this.form = new Form(this);
        this.generator = new CLGenerator();
    }

    public void bind() {
        Map<String, String> replacementContent = new HashMap<>();

        form.getSubmitButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replacementContent.put("<COMPANY>", form.getCompany().getText());
                replacementContent.put("<POSITION>", form.getPosition().getText());
                replacementContent.put("<CUSTOM_TEXT>", form.getCustomText().getText());
                replacementContent.put("<TECHNOLOGY>", Utils.jListToString(form.getTechList()));

                generator.setReplacementContent(replacementContent);
                generator.setFileDst(form.getFileDst());

                try {
                    generator.generate();
                    JOptionPane.showMessageDialog(form, "Cover letter was generated successfully!");
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(form, "Generation failed.");
                    System.err.println("Cover Letter generation failed");
                    err.printStackTrace();
                }
            }
        });
    }

    public Form buildFormView() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        File coverLetter;

        if (fileChooser.showOpenDialog(form) == JFileChooser.CANCEL_OPTION) {
            System.exit(1);
        }

        coverLetter = fileChooser.getSelectedFile();
        generator.setCoverLetterTemplate(coverLetter);

        //TODO: allow user to set custom cover letter name
        //generator.setFileName(coverLetter.getName());
        form.build();

        return form;
    }
}
