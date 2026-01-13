package frontend.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Pulsante personalizzato per l'apertura del selettore di file (File Chooser).
 * <p>
 * Estende {@link IconButton} e fornisce la funzionalità per selezionare un'immagine
 * dal file system locale. Gestisce internamente la configurazione del {@link JFileChooser},
 * applicando filtri per estensione (JPG, PNG, GIF) e controlli sulla dimensione massima del file (2MB).
 * In caso di selezione valida, notifica il {@link FileChooserPanel} padre; altrimenti mostra un messaggio di errore.
 * </p>
 */
public class FileChooserButton extends IconButton{

    /**
     * Il componente Swing standard per la navigazione e selezione dei file.
     */
    JFileChooser fileChooser;

    /**
     * Riferimento al pannello gestore che riceverà il file selezionato.
     */
    FileChooserPanel fileChooserPanel;

    /**
     * Costruttore del pulsante.
     * <p>
     * Inizializza il pulsante con l'icona specifica e registra un {@code ActionListener}
     * che invoca il metodo {@link #openFileChooser()} al click.
     * </p>
     *
     * @param fileChooserPanel Il pannello genitore a cui passare il file validato.
     */
    public FileChooserButton(FileChooserPanel fileChooserPanel) {

        super("/frontend/gui/images/fileChooserIcon.svg", 40, 40);

        this.fileChooserPanel = fileChooserPanel;

        addActionListener(e -> openFileChooser());
    }

    /**
     * Apre la finestra di dialogo per la selezione del file ed esegue le validazioni.
     * <p>
     * Il flusso di esecuzione è il seguente:
     * <ol>
     * <li>Configura il file chooser.</li>
     * <li>Mostra il dialog di apertura.</li>
     * <li>Se l'utente conferma la selezione:
     * <ul>
     * <li>Verifica che la dimensione del file sia inferiore o uguale a 2MB.</li>
     * <li>Verifica che l'estensione sia supportata (.jpg, .jpeg, .png, .gif).</li>
     * </ul>
     * </li>
     * <li>Se valido, passa il file al {@code fileChooserPanel}.</li>
     * <li>Se non valido, mostra un {@link FloatingMessage} di errore specifico.</li>
     * </ol>
     * </p>
     */
    private void openFileChooser() {

        setFileChooser();

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            String fileName = fileChooser.getSelectedFile().getName().toLowerCase();

            // Controllo dimensione: limite 2MB (2 * 1024 * 1024 bytes)
            if (fileChooser.getSelectedFile().length() <= 2 * 1024 * 1024) {
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                        fileName.endsWith(".png") || fileName.endsWith(".gif"))
                    fileChooserPanel.setSelectedFile(fileChooser.getSelectedFile());
                else
                    new FloatingMessage("Formato del file non supportato", this, FloatingMessage.ERROR_MESSAGE);
            } else
                new FloatingMessage("L'immagine è troppo pesante, selezionare un'immagine con dimensione non superiore a 2MB", this, FloatingMessage.ERROR_MESSAGE);
        }
    }

    /**
     * Inizializza e configura l'istanza di {@link JFileChooser}.
     * <p>
     * Imposta il titolo del dialog, disabilita la selezione multipla e applica
     * un filtro per mostrare solo i file immagine supportati.
     * </p>
     */
    private void setFileChooser() {

        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona un'immagine");
        fileChooser.setMultiSelectionEnabled(false);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Immagini (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
    }
}