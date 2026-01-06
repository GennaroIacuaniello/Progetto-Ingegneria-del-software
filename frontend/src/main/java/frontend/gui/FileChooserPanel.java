package frontend.gui;

import lombok.Getter;

import java.awt.*;
import java.io.File;

public class FileChooserPanel extends RoundedPanel {

    FileChooserButton fileChooserButton;
    @Getter
    File selectedFile;
    FileButton fileButton;

    public FileChooserPanel() {

        super(new GridBagLayout());

        setRoundBorderColor(ColorsList.EMPTY_COLOR);
        setBackground(ColorsList.EMPTY_COLOR);

        setFileChooserButton();
        setFileButton();
    }

    public void setFileChooserButton() {

        fileChooserButton = new FileChooserButton(this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(fileChooserButton, Constraints.getGridBagConstraints());
    }

    public void setFileButton() {

        fileButton = new FileButton(this);

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(fileButton, Constraints.getGridBagConstraints());
    }

    public void setSelectedFile(File file) {

        this.selectedFile = file;
        fileButton.updateFile(file);
    }

    public void removeFile() {

        this.selectedFile = null;
        fileButton.updateFile(null);
    }
}
