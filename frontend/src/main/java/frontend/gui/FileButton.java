package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Componente grafico che rappresenta un file selezionato all'interno dell'interfaccia.
 * <p>
 * Questa classe estende {@link JPanel} e visualizza il nome del file corrente accanto a un
 * pulsante di rimozione. Offre funzionalità interattive:
 * </p>
 * <ul>
 * <li>Cliccando sul nome del file, si tenta di aprirlo con l'applicazione di sistema predefinita.</li>
 * <li>Cliccando sul pulsante "X", il file viene rimosso dalla selezione (tramite callback al {@code parentPanel}).</li>
 * </ul>
 * <p>
 * Il componente gestisce automaticamente la propria visibilità in base alla presenza o meno di un file.
 * </p>
 */
public class FileButton extends JPanel {

    /**
     * Etichetta cliccabile che mostra il nome del file.
     */
    private JLabel fileNameLabel;

    /**
     * Pulsante icona per rimuovere il file dalla selezione.
     */
    private IconButton removeButton;

    /**
     * Riferimento al file attualmente visualizzato.
     */
    private File currentFile;

    /**
     * Riferimento al pannello genitore che gestisce la logica di scelta dei file.
     */
    private final FileChooserPanel parentPanel;

    /**
     * Costruttore del componente FileButton.
     * <p>
     * Inizializza il layout (FlowLayout allineato a sinistra), configura i componenti interni
     * e imposta la visibilità iniziale a {@code false} (nascosto finché non viene caricato un file).
     * </p>
     *
     * @param parent Il pannello {@link FileChooserPanel} che contiene questo componente.
     */
    public FileButton(FileChooserPanel parent) {

        this.parentPanel = parent;

        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setOpaque(false);
        setVisible(false);

        setRemoveButton();
        setFileNameLabel();
    }

    /**
     * Configura e aggiunge il pulsante di rimozione (icona "X").
     * <p>
     * Aggiunge un listener che, al click, invoca il metodo {@code removeFile()} del pannello genitore.
     * </p>
     */
    private void setRemoveButton() {

        removeButton = new IconButton("/frontend/gui/images/xIconButton.png", 10, 10);

        removeButton.addActionListener(e -> parentPanel.removeFile());

        add(removeButton);
    }

    /**
     * Configura e aggiunge l'etichetta per il nome del file.
     * <p>
     * Imposta il cursore a "mano" per indicare l'interattività e aggiunge un MouseListener
     * per gestire l'apertura del file al click.
     * </p>
     */
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

    /**
     * Aggiorna il componente con un nuovo file.
     * <p>
     * Se il file passato è diverso da null:
     * </p>
     * <ul>
     * <li>Aggiorna il testo dell'etichetta con il nome del file.</li>
     * <li>Rende il componente visibile.</li>
     * </ul>
     * <p>
     * Se il file è null, nasconde il componente.
     * </p>
     *
     * @param file Il file da visualizzare, o {@code null} per resettare il componente.
     */
    public void updateFile(File file) {

        currentFile = file;

        if (currentFile != null)
            fileNameLabel.setText(currentFile.getName());

        setVisible(currentFile != null);

        revalidate();
        repaint();
    }

    /**
     * Tenta di aprire il file corrente utilizzando l'applicazione predefinita del sistema operativo.
     * <p>
     * Utilizza la classe {@link Desktop}. Se l'apertura fallisce (es. file non trovato o nessuna
     * applicazione associata), viene mostrato un {@link FloatingMessage} di errore.
     * </p>
     */
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