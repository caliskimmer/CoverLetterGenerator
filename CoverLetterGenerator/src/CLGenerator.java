import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

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

        if (this.fileName == null) {
            this.fileName = this.replacementContent.get("<COMPANY>") + ".docx";
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String replaceKeywords(Map<String, String> replacementContent) {
        String content = retrieveDocumentContent();

        for (Map.Entry<String, String> pair : replacementContent.entrySet()) {
            content = content.replaceFirst(pair.getKey(), pair.getValue());
        }

        return content;
    }

    private String retrieveDocumentContent() {
        XWPFWordExtractor extractor = null;
        try {
            FileInputStream fis = new FileInputStream(coverLetterTemplate.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            extractor = new XWPFWordExtractor(document);
        } catch (Exception e) {
            //TODO: Might want to improve on this...
            e.printStackTrace();
        }

        return extractor.getText();
    }

    private  void buildCoverLetter(String fileName, File template, String content) throws IOException {
        String[] splitContent = content.split("\n");
        XWPFDocument document = new XWPFDocument();

        for (String partial : splitContent) {
            XWPFParagraph buffer = document.createParagraph();
            XWPFRun bufferRun = buffer.createRun();

            if (!partial.isEmpty()) {
                bufferRun.setText(partial);
            }

            bufferRun.setFontSize(12);
        }


        FileOutputStream fos = new FileOutputStream(new File(fileName));
        document.write(fos);
        fos.close();
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
