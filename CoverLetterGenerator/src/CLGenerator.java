import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class CLGenerator {
    private File coverLetterTemplate;
    private Map<String, String> replacementContent;
    private String fileName;

    //Strings represent keywords to replace in template resume
    private final String CUSTOM_TEXT  = "<CUSTOM_TEXT>";
    private final String TECHNOLOGIES = "<TECHNOLOGIES>";
    private final String COMPANY      = "<COMPANY>";
    private final String POSITION     = "<POSITION>";
    private final String[] KEYWORDS = {COMPANY, POSITION, TECHNOLOGIES, CUSTOM_TEXT};

    public CLGenerator() {}

    public CLGenerator(File coverLetterTemplate, Map<String, String> replacementContent, String fileName) {
        this.coverLetterTemplate = coverLetterTemplate;
        this.replacementContent = replacementContent;
        this.fileName = fileName;
    }

    public CLGenerator(File coverLetterTemplate, Map<String,String> replacementContent) {
        this.coverLetterTemplate = coverLetterTemplate;
        this.replacementContent = replacementContent;
        this.fileName = this.replacementContent.get("<COMPANY>") + ".docx";
    }

    public void setCoverLetterTemplate(File coverLetterTemplate) {
        this.coverLetterTemplate = coverLetterTemplate;
    }

    public void setReplacementContent(Map<String, String> replacementContent) {
        this.replacementContent = replacementContent;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String replaceKeywords(Map<String, String> replacementContent) {
        String content = null;

        try {
            content = new String(Files.readAllBytes(Paths.get(coverLetterTemplate.getAbsolutePath())));

            for (Map.Entry<String, String> pair : replacementContent.entrySet()) {
                content.replaceFirst(pair.getValue(), pair.getKey());
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

    public boolean generate() throws IOException{
        if (this.fileName == null || this.replacementContent == null || this.coverLetterTemplate == null) {
            return false;
        }

        if (!coverLetterTemplate.exists()) {
            return false;
        }

        String content = replaceKeywords(replacementContent);
        buildCoverLetter(fileName, coverLetterTemplate, content);

        return true;
    }
}
