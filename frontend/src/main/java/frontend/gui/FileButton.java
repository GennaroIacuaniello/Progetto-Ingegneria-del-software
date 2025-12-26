package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class FileButton extends JPanel {

    private JLabel fileNameLabel;
    private IconButton removeButton;
    private File currentFile;
    private final FileChooserPanel parentPanel;

    public FileButton(FileChooserPanel parent) {

        this.parentPanel = parent;

        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setOpaque(false);
        setVisible(false);

        setRemoveButton();
        setFileNameLabel();
    }

    private void setRemoveButton() {

        removeButton = new IconButton("/frontend/gui/images/xIconButton.png", 10, 10);

        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeFile();
            }
        });

        add(removeButton);
    }

    private void setFileNameLabel() {

        fileNameLabel = new JLabel("");

        fileNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        fileNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openFile();
            }
        });

        add(fileNameLabel);
    }

    public void updateFile(File file) {

        currentFile = file;

        if (currentFile != null)
            fileNameLabel.setText(currentFile.getName());

        setVisible(currentFile != null);

        revalidate();
        repaint();
    }

    private void openFile() {
        if (currentFile != null && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(currentFile);
            } catch (IOException ex) {
                new FloatingMessage("Impossibile aprire il file.", removeButton, FloatingMessage.ERROR_MESSAGE);
            }
        }
    }
}
