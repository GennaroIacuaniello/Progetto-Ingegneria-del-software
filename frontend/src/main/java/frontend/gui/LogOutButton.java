package frontend.gui;

import frontend.controller.AuthController;

import javax.swing.*;

/**
 * Componente grafico che rappresenta il pulsante di disconnessione (Logout).
 * <p>
 * Estende {@link IconButton} per visualizzare l'icona di uscita specifica.
 * Al click, questo pulsante gestisce l'intero flusso di disconnessione:
 * </p>
 * <ol>
 * <li>Chiede conferma all'utente tramite un dialog modale.</li>
 * <li>Se confermato, resetta l'utente corrente nel {@link AuthController}.</li>
 * <li>Chiude la finestra principale e riporta l'applicazione alla schermata di Login.</li>
 * </ol>
 */
public class LogOutButton extends IconButton {

    /**
     * Costruttore del pulsante di Logout.
     * <p>
     * Inizializza il pulsante con l'icona specifica ("logOutButtonIcon.svg") e le dimensioni standard (50x50).
     * Imposta immediatamente il listener per gestire il click.
     * </p>
     *
     * @param mainFrame Il frame principale dell'applicazione, necessario per centrare il dialog di conferma e per essere chiuso al termine del logout.
     */
    public LogOutButton(JFrame mainFrame) {

        super("/frontend/gui/images/logOutButtonIcon.svg", 50, 50);

        setActionListener(mainFrame);
    }

    /**
     * Configura l'azione da eseguire al click del pulsante.
     * <p>
     * La logica implementata è la seguente:
     * <ul>
     * <li>Mostra un {@link JOptionPane} di conferma ("Sei sicuro di voler uscire?").</li>
     * <li>Se l'utente seleziona "Sì" (YES_OPTION):
     * <ul>
     * <li>Imposta l'utente loggato a {@code null} nel controller.</li>
     * <li>Chiude (dispose) il frame principale corrente.</li>
     * <li>Crea e mostra una nuova istanza di {@link LogInPage}.</li>
     * </ul>
     * </li>
     * </ul>
     * L'operazione di chiusura e riapertura è avvolta in {@code SwingUtilities.invokeLater} per garantire
     * la corretta gestione del thread grafico (EDT).
     * </p>
     *
     * @param mainFrame Il frame su cui operare.
     */
    private void setActionListener(JFrame  mainFrame) {

        this.addActionListener(e -> {

            int response = JOptionPane.showConfirmDialog(mainFrame,
                    "Sei sicuro di voler uscire?", "Conferma LogOut",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {

                SwingUtilities.invokeLater(() -> {
                    // Resetta la sessione
                    AuthController.getInstance().setLoggedUser(null);
                    // Chiude la dashboard
                    mainFrame.dispose();
                    // Torna al login
                    new LogInPage().setVisible(true);
                });
            }
        });
    }
}