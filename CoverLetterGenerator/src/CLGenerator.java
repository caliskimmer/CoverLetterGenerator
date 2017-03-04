import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class CLGenerator {
    private File coverLetterTemplate;
    private List<String> replacementContent;
    private String fileName;

    //Strings represent keywords to replace in template resume
    private final String CUSTOM_TEXT  = "<CUSTOM_TEXT>";
    private final String TECHNOLOGIES = "<TECHNOLOGIES>";
    private final String COMPANY      = "<COMPANY>";
    private final String POSITION     = "<POSITION>";
    private final String[] KEYWORDS = {COMPANY, POSITION, TECHNOLOGIES, CUSTOM_TEXT};

    public CLGenerator(File coverLetterTemplate, ArrayList<String> replacementContent, String fileName) {
        this.coverLetterTemplate = coverLetterTemplate;
        this.replacementContent = replacementContent;
        this.fileName = fileName;
    }

    public CLGenerator(File coverLetterTemplate, ArrayList<String> replacementContent) {
        this.coverLetterTemplate = coverLetterTemplate;
        this.replacementContent = replacementContent;
        this.fileName = "GeneratedCoverLetter.docx";
    }

    private String replaceKeywords(List<String> replacementContent) {
        String content = null;

        try {
            content = new String(Files.readAllBytes(Paths.get(coverLetterTemplate.getAbsolutePath())));

            for (int ii = 0; ii < replacementContent.size(); ii++) {
                content.replaceFirst(replacementContent.get(ii), KEYWORDS[ii]);
            }

        } catch (IOException e) {
            //change this line for more proper error detection!
            System.err.println("File not found");
        }

        return content;
    }

    private  void buildCoverLetter(String fileName, File template, String content) throws IOException {
        File newCoverLetter = new File(template.getName()+fileName+".docx");
        FileWriter fWriter = new FileWriter(newCoverLetter.getName());
        BufferedWriter out = new BufferedWriter(fWriter);
        out.write(content);
        out.close();
    }

    public boolean generate() {
        if (!this.coverLetterTemplate.exists()) {
            return false;
        }

        String content = replaceKeywords(this.replacementContent);

        try {
            buildCoverLetter(this.fileName, this.coverLetterTemplate, content);
        } catch (IOException e) {
            System.err.println("Cover Letter generation failed");
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
