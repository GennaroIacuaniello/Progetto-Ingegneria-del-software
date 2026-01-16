package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Vista di dettaglio per le Issue Assegnate (Lato Sviluppatore).
 * <p>
 * Questa classe estende {@link ShowReportedIssueDeveloper} per visualizzare i dettagli completi
 * di una segnalazione (inclusi priorità e allegati).
 * <br>
 * La caratteristica distintiva di questa classe è l'aggiunta di un pulsante operativo ("Segna come risolta"),
 * che permette allo sviluppatore assegnatario di completare il task e far avanzare lo stato dell'issue.
 * </p>
 */
public class ShowAssignedIssue extends ShowReportedIssueDeveloper {

    /**
     * Pulsante per la risoluzione.
     */
    private JButton resolveButton;

    /**
     * Rounded panel.
     */
    private RoundedPanel tmpPanel;

    /**
     * Costruttore della vista issue assegnata.
     * <p>
     * 1. Richiama il costruttore della superclasse per disegnare la scheda completa dell'issue.<br>
     * 2. Esegue un controllo sullo stato attuale dell'issue: il pulsante di risoluzione viene
     * mostrato <b>solo</b> se l'issue è attualmente nello stato "ASSIGNED". Se l'issue fosse già
     * risolta o chiusa, l'azione non sarebbe necessaria.
     * </p>
     *
     * @param parent Il frame genitore (necessario per le finestre di dialogo modali).
     */
    public ShowAssignedIssue(JFrame parent) {

        super(parent);

        // Aggiunge il pulsante di azione solo se l'issue è ancora in lavorazione
        if (statusLabel.getText().equals("Stato: ASSIGNED"))
            setResolveButton();
    }

    /**
     * Crea, stilizza e posiziona il pulsante "Segna come risolta".
     * <p>
     * Il pulsante viene stilizzato con un colore verde per indicare un'azione positiva (completamento).
     * Viene posizionato in basso nel pannello principale.
     * </p>
     */
    private void setResolveButton() {

        resolveButton = new JButton("Segna come risolta");

        resolveButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        resolveButton.setBorder(BorderFactory.createEmptyBorder());
        resolveButton.setBackground(ColorsList.EMPTY_COLOR);

        resolveButton.addActionListener(e -> resolve());

        tmpPanel = ContainerFactory.createRoundedPanelContainer(resolveButton);

        tmpPanel.setRoundBorderColor(ColorsList.GREEN_BORDER_COLOR);
        tmpPanel.setBackground(ColorsList.GREEN_BACKGROUND_COLOR);
        tmpPanel.setCursor(new  Cursor(Cursor.HAND_CURSOR));

        Constraints.setConstraints(0, 5, 4, 1,
                GridBagConstraints.NONE, 40, 20, GridBagConstraints.CENTER,
                0.5f, 0.5f, new Insets(5, 5, 5, 5));
        mainPanel.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Esegue la logica di risoluzione dell'issue.
     * <p>
     * 1. Invoca {@link IssueController#setIssueAsResolved()} per aggiornare lo stato nel backend.<br>
     * 2. Se l'operazione ha successo:
     * <ul>
     * <li>Aggiorna immediatamente l'etichetta di stato nella GUI a "Risolta".</li>
     * <li>Mostra un messaggio fluttuante di successo.</li>
     * <li>Nasconde il pulsante di risoluzione (l'azione non è più ripetibile).</li>
     * </ul>
     * </p>
     */
    private void resolve() {

        boolean success = IssueController.getInstance().setIssueAsResolved();

        if(!success)
            return;

        statusLabel.setText("Stato: Risolta");
        resolutionDateLabel.setText("Risoluzione: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        new FloatingMessage("Segnalazione avvenuta con successo", resolveButton, FloatingMessage.SUCCESS_MESSAGE);
        tmpPanel.setVisible(false);
    }
}