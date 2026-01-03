package frontend.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChooserButton extends IconButton{

    JFileChooser fileChooser;
    FileChooserPanel fileChooserPanel;

    public FileChooserButton(FileChooserPanel fileChooserPanel) {

        super("/frontend/gui/images/fileChooserIcon.svg", 40, 40);

        this.fileChooserPanel = fileChooserPanel;

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                openFileChooser();
            }
        });
    }

    private void openFileChooser() {

        setFileChooser();

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            String fileName = fileChooser.getSelectedFile().getName().toLowerCase();

            if (fileChooser.getSelectedFile().length() <= 2 * 1024 * 1024) {
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                        fileName.endsWith(".png") || fileName.endsWith(".gif"))
                    fileChooserPanel.setSelectedFile(fileChooser.getSelectedFile());
                else
                    new FloatingMessage("Formato del file non supportato", this, FloatingMessage.ERROR_MESSAGE);
            } else
                new FloatingMessage("L'immagine è troppo pesante, selezionare un'immagine con dimensione non superiore a ", this, FloatingMessage.ERROR_MESSAGE);
        }
    }

    private void setFileChooser() {

        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona un'immagine");
        fileChooser.setMultiSelectionEnabled(false);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Immagini (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
    }
}
