import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.io.File;


public class Form extends JPanel{
    private final int SECTION_WIDTH = 850;
    private final int DEFAULT_PADDING = 10;
    private final int MAX_TF_WIDTH = 15;
    private final int TF_RIGHT_MARGIN = -100;

    private JPanel companySection;
    private JPanel technologySection;
    private JPanel customTextSection;
    private JPanel settingsSection;
    private JLabel lDestinationOptionPath;
    private JTextField tfCompany;
    private JTextField tfPosition;
    private JList<String> techList;
    private JTextArea taCustomText;
    private FormController controller;
    private JFileChooser fileChooser;
    private JButton submit;

    public Form() {
        super();
    }

    public Form(FormController controller) {
        this.controller = controller;
    }

    public JButton getSubmitButton() {
        return submit;
    }

    public JTextField getCompany() {
        return tfCompany;
    }

    public JTextField getPosition() {
        return tfPosition;
    }

    public JTextArea getCustomText() {
        return taCustomText;
    }

    public JList<String> getTechList() {
        return techList;
    }

    public String getFileDst() {
        if (lDestinationOptionPath.getText().equals("") || lDestinationOptionPath.getText().equals("No path selected")) {
            return null;
        }

        return lDestinationOptionPath.getText();
    }

    private JPanel initSection(Dimension d) {
        SpringLayout layout = new SpringLayout();
        JPanel newPanel = new JPanel();
        newPanel.setLayout(layout);
        newPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        newPanel.setMaximumSize(d);

        return newPanel;
    }

    private void buildSettingsSection() {
        final int SETTINGS_SECTION_HEIGHT = 60;
        final int ALIGNMENT_TO_BUTTON_PADDING = DEFAULT_PADDING + 5;

        settingsSection = initSection(new Dimension(SECTION_WIDTH, SETTINGS_SECTION_HEIGHT));
        SpringLayout layout = (SpringLayout) settingsSection.getLayout();

        JButton fileButton = new JButton("Save As...");

        lDestinationOptionPath = new JLabel("No path selected");

        settingsSection.add(lDestinationOptionPath);
        settingsSection.add(fileButton);

        layout.putConstraint(SpringLayout.WEST, fileButton, DEFAULT_PADDING, SpringLayout.WEST, settingsSection);
        layout.putConstraint(SpringLayout.WEST, lDestinationOptionPath, DEFAULT_PADDING, SpringLayout.EAST, fileButton);

        layout.putConstraint(SpringLayout.NORTH, fileButton, DEFAULT_PADDING, SpringLayout.NORTH, settingsSection);
        layout.putConstraint(SpringLayout.NORTH, lDestinationOptionPath, ALIGNMENT_TO_BUTTON_PADDING, SpringLayout.NORTH, settingsSection);

        fileButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int selection = fileChooser.showOpenDialog(Form.this);

                if (selection == JFileChooser.APPROVE_OPTION) {
                    lDestinationOptionPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        add(settingsSection);
    }

    private void buildCompanySection() {
        final int COMPANY_SECTION_HEIGHT = 100;

        companySection = initSection(new Dimension(SECTION_WIDTH, COMPANY_SECTION_HEIGHT));
        SpringLayout layout = (SpringLayout) companySection.getLayout();

        JLabel lCompany = new JLabel("Company");
        JLabel lPosition = new JLabel("Position");

        tfCompany = new JTextField(MAX_TF_WIDTH);
        tfPosition = new JTextField(MAX_TF_WIDTH);

        lCompany.setLabelFor(tfCompany);
        lPosition.setLabelFor(tfPosition);

        companySection.add(lCompany);
        companySection.add(lPosition);

        companySection.add(tfCompany);
        companySection.add(tfPosition);

        layout.putConstraint(SpringLayout.WEST, lCompany, DEFAULT_PADDING, SpringLayout.WEST, companySection);
        layout.putConstraint(SpringLayout.EAST, tfCompany, TF_RIGHT_MARGIN, SpringLayout.EAST, companySection);
        layout.putConstraint(SpringLayout.WEST, lPosition, DEFAULT_PADDING, SpringLayout.WEST, companySection);
        layout.putConstraint(SpringLayout.EAST, tfPosition, TF_RIGHT_MARGIN, SpringLayout.EAST, companySection);

        layout.putConstraint(SpringLayout.NORTH, lCompany, DEFAULT_PADDING, SpringLayout.NORTH, companySection);
        layout.putConstraint(SpringLayout.NORTH, tfCompany, DEFAULT_PADDING, SpringLayout.NORTH, companySection);
        layout.putConstraint(SpringLayout.NORTH, lPosition, DEFAULT_PADDING, SpringLayout.SOUTH, tfCompany);
        layout.putConstraint(SpringLayout.NORTH, tfPosition, DEFAULT_PADDING, SpringLayout.SOUTH, tfCompany);

        add(companySection);
    }

    private void buildTechnologySection() {
        final int TECH_SECTION_HEIGHT = 200;
        final int TECH_LIST_WIDTH = 190;
        final int TECH_LIST_HEIGHT = 100;
        final int TECH_BUTTON_PADDING = 5;

        technologySection = initSection(new Dimension(SECTION_WIDTH,TECH_SECTION_HEIGHT));
        SpringLayout layout = (SpringLayout) technologySection.getLayout();

        JLabel lTechnology = new JLabel("Technology");
        JLabel lList = new JLabel ("List");

        JTextField tfTechnology = new JTextField(MAX_TF_WIDTH);
        DefaultListModel listModel = new DefaultListModel();
        techList = new JList<String>(listModel);
        Map<String, Boolean> listHelper = new HashMap<>();

        techList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        techList.setLayoutOrientation(JList.VERTICAL);

        JScrollPane techListPane = new JScrollPane(techList);
        techListPane.setPreferredSize(new Dimension(TECH_LIST_WIDTH, TECH_LIST_HEIGHT));

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tfTechnologyInput = tfTechnology.getText();

                if (tfTechnologyInput != null) {
                    if (!listHelper.containsKey(tfTechnologyInput.toLowerCase())) {
                        listModel.addElement(tfTechnologyInput);
                        listHelper.put(tfTechnologyInput.toLowerCase(), true);
                        tfTechnology.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(Form.this, "Can't add the same technology twice!");
                    }
                }
            }
        });

        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int toRemove = techList.getSelectedIndex();

                if (toRemove >= 0) {
                    listModel.removeElementAt(toRemove);
                }
            }
        });

        lTechnology.setLabelFor(tfTechnology);
        lList.setLabelFor(techList);

        technologySection.add(lTechnology);
        technologySection.add(lList);

        technologySection.add(tfTechnology);
        technologySection.add(techListPane);

        technologySection.add(addButton);
        technologySection.add(deleteButton);

        layout.putConstraint(SpringLayout.WEST, lTechnology, DEFAULT_PADDING, SpringLayout.WEST, technologySection);
        layout.putConstraint(SpringLayout.EAST, tfTechnology, TF_RIGHT_MARGIN, SpringLayout.EAST, technologySection);
        layout.putConstraint(SpringLayout.WEST, addButton, DEFAULT_PADDING, SpringLayout.EAST, tfTechnology);
        layout.putConstraint(SpringLayout.EAST, lList, TF_RIGHT_MARGIN-160, SpringLayout.EAST, technologySection);
        layout.putConstraint(SpringLayout.EAST, techListPane, TF_RIGHT_MARGIN, SpringLayout.EAST, technologySection);
        layout.putConstraint(SpringLayout.WEST, deleteButton, DEFAULT_PADDING, SpringLayout.EAST, techListPane);

        layout.putConstraint(SpringLayout.NORTH, lTechnology, DEFAULT_PADDING, SpringLayout.NORTH, technologySection);
        layout.putConstraint(SpringLayout.NORTH, tfTechnology, DEFAULT_PADDING, SpringLayout.NORTH, technologySection);
        layout.putConstraint(SpringLayout.NORTH, addButton, DEFAULT_PADDING, SpringLayout.NORTH, technologySection);
        layout.putConstraint(SpringLayout.NORTH, lList, DEFAULT_PADDING, SpringLayout.SOUTH, tfTechnology);
        layout.putConstraint(SpringLayout.NORTH, techListPane, TECH_BUTTON_PADDING, SpringLayout.SOUTH, lList);
        layout.putConstraint(SpringLayout.NORTH, deleteButton, TECH_BUTTON_PADDING, SpringLayout.SOUTH, lList);

        add(technologySection);
    }

    private void buildCustomTextSection() {
        final int CUSTOM_TEXT_WIDTH = 280;
        final int CUSTOM_TEXT_HEIGHT = 135;
        final int TEXT_PANE_HEIGHT = 100;

        customTextSection = initSection(new Dimension(SECTION_WIDTH, CUSTOM_TEXT_HEIGHT));
        SpringLayout layout = (SpringLayout) customTextSection.getLayout();

        JLabel lCustomText = new JLabel("Custom Text");
        taCustomText = new JTextArea();
        taCustomText.setWrapStyleWord(true);
        taCustomText.setLineWrap(true);
        JScrollPane customTextPane = new JScrollPane(taCustomText);

        customTextPane.setPreferredSize(new Dimension(CUSTOM_TEXT_WIDTH, TEXT_PANE_HEIGHT));

        lCustomText.setLabelFor(taCustomText);

        customTextSection.add(lCustomText);
        customTextSection.add(customTextPane);

        layout.putConstraint(SpringLayout.WEST, lCustomText, DEFAULT_PADDING, SpringLayout.WEST, customTextSection);
        layout.putConstraint(SpringLayout.EAST, customTextPane, -DEFAULT_PADDING, SpringLayout.EAST, customTextSection);
        layout.putConstraint(SpringLayout.NORTH, lCustomText, DEFAULT_PADDING, SpringLayout.NORTH, customTextSection);
        layout.putConstraint(SpringLayout.NORTH, customTextPane, DEFAULT_PADDING, SpringLayout.NORTH, customTextSection);

        add(customTextSection);
    }


    public void build() {
        final int FORM_HEIGHT = 400;
        final int FORM_WIDTH = 500;

        Dimension formDimensions = new Dimension(FORM_HEIGHT, FORM_WIDTH);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(formDimensions);

        buildCompanySection();
        buildTechnologySection();
        buildCustomTextSection();
        buildSettingsSection();

        submit = new JButton("Submit");

        controller.bind();
        add(submit);
    }

}
