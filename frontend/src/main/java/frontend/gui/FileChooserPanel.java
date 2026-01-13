package frontend.gui;

import lombok.Getter;

import java.awt.*;
import java.io.File;

/**
 * Pannello composito per la selezione e gestione di un file allegato.
 * <p>
 * Questa classe estende {@link RoundedPanel} e funge da contenitore logico e grafico per:
 * </p>
 * <ul>
 * <li>Il pulsante di apertura del selettore file ({@link FileChooserButton}).</li>
 * <li>Il componente che mostra il nome del file selezionato e permette di rimuoverlo ({@link FileButton}).</li>
 * </ul>
 * <p>
 * Mantiene lo stato del file correntemente selezionato e coordina l'aggiornamento dell'interfaccia
 * quando un file viene aggiunto o rimosso.
 * </p>
 */
public class FileChooserPanel extends RoundedPanel {

    /**
     * Il pulsante che apre il JFileChooser.
     */
    FileChooserButton fileChooserButton;

    /**
     * Il file attualmente selezionato dall'utente.
     * <p>
     * Può essere {@code null} se nessun file è stato scelto o se è stato rimosso.
     * Il getter è generato automaticamente da Lombok.
     * </p>
     */
    @Getter
    File selectedFile;

    /**
     * Il componente grafico che visualizza il nome del file e il tasto "rimuovi".
     */
    FileButton fileButton;

    /**
     * Costruttore del pannello.
     * <p>
     * Configura il layout (GridBagLayout), imposta lo sfondo come trasparente (EMPTY_COLOR)
     * e inizializza i due componenti figli (pulsante di scelta e visualizzatore file).
     * </p>
     */
    public FileChooserPanel() {

        super(new GridBagLayout());

        setRoundBorderColor(ColorsList.EMPTY_COLOR);
        setBackground(ColorsList.EMPTY_COLOR);

        setFileChooserButton();
        setFileButton();
    }

    /**
     * Inizializza e aggiunge al pannello il pulsante per la selezione del file.
     */
    public void setFileChooserButton() {

        fileChooserButton = new FileChooserButton(this);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(fileChooserButton, Constraints.getGridBagConstraints());
    }

    /**
     * Inizializza e aggiunge al pannello il componente per la visualizzazione del file.
     * <p>
     * Inizialmente questo componente sarà invisibile finché non viene impostato un file.
     * </p>
     */
    public void setFileButton() {

        fileButton = new FileButton(this);

        Constraints.setConstraints(1, 0, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        this.add(fileButton, Constraints.getGridBagConstraints());
    }

    /**
     * Imposta il file selezionato e aggiorna l'interfaccia.
     * <p>
     * Questo metodo viene invocato dal {@link FileChooserButton} quando l'utente
     * completa con successo la selezione di un file valido.
     * Aggiorna lo stato interno e notifica il {@link FileButton} per mostrare il nome del file.
     * </p>
     *
     * @param file Il file selezionato.
     */
    public void setSelectedFile(File file) {

        this.selectedFile = file;
        fileButton.updateFile(file);
    }

    /**
     * Rimuove il file selezionato e resetta l'interfaccia.
     * <p>
     * Questo metodo viene invocato dal {@link FileButton} quando l'utente clicca sulla "X".
     * Imposta il file selezionato a {@code null} e nasconde il componente di visualizzazione.
     * </p>
     */
    public void removeFile() {

        this.selectedFile = null;
        fileButton.updateFile(null);
    }
}