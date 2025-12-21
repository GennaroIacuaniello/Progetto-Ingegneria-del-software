package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileChooserButton extends IconButton{

    FileChooserPanel fileChooserPanel;

    public FileChooserButton(FileChooserPanel fileChooserPanel) {

        super("/gui/images/fileChooserIcon.png", 30, 30);

        this.fileChooserPanel = fileChooserPanel;

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                openFileChooser();
            }
        });
    }

    private void openFileChooser() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona un'immagine");
        fileChooser.setMultiSelectionEnabled(false);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Immagini (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            fileChooserPanel.setSelectedFile(fileChooser.getSelectedFile());
    }
}
