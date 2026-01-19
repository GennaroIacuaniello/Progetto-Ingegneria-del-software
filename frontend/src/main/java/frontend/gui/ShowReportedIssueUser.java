package frontend.gui;

import frontend.controller.IssueController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Finestra di dialogo per la visualizzazione dei dettagli di una Issue (Lato Utente).
 * <p>
 * Questa classe estende {@link MyDialog} (una finestra modale personalizzata) e si occupa di
 * recuperare tutti i dati della issue corrente tramite {@link IssueController} e impaginarli
 * in un layout organizzato.
 * <br>
 * Funzionalità principali:
 * </p>
 * <ul>
 * <li>Visualizzazione di Titolo e Descrizione (in area di testo scorrevole non modificabile).</li>
 * <li>Visualizzazione dei metadati: Autore, Date (segnalazione/risoluzione), Tipo, Stato.</li>
 * <li>Visualizzazione del Developer assegnato (se presente).</li>
 * <li><b>Gestione Allegati:</b> Pulsante per aprire l'immagine allegata col visualizzatore di sistema.</li>
 * <li><b>Gestione Tag:</b> Pulsante che apre un menu popup con la lista dei tag.</li>
 * </ul>
 * <p>
 * I campi {@code statusLabel} e {@code assignedDeveloperLabel} sono {@code protected} per permettere
 * alle sottoclassi (Admin/Developer) di aggiornarli dinamicamente dopo azioni di modifica.
 * </p>
 */
public class ShowReportedIssueUser extends MyDialog {

    /**
     * Etichetta dello stato. Protetta per consentire l'aggiornamento immediato
     * se una sottoclasse cambia lo stato (es. da "Assegnata" a "Risolta").
     */
    protected JLabel statusLabel;

    /**
     * Etichetta del developer assegnato. Protetta per consentire l'aggiornamento immediato
     * se l'Admin assegna l'issue dalla sua vista estesa.
     */
    protected JLabel assignedDeveloperLabel;

    /**
     * Etichetta del tempo di risoluzione. Protetta per consentire l'aggiornamento immediato
     * se l'Admin senga l'issue come risolta dalla sua vista estesa.
     */
    protected JLabel resolutionDateLabel;

    /**
     * Costruttore della finestra di dettaglio.
     * <p>
     * Costruisce l'interfaccia chiamando in sequenza i metodi di setup per ogni componente.
     * I dati vengono prelevati "al volo" dal Singleton {@link IssueController}, che mantiene
     * lo stato dell'issue attualmente selezionata.
     * </p>
     *
     * @param parent Il frame genitore (per rendere il dialogo modale rispetto ad esso).
     */
    public ShowReportedIssueUser(JFrame parent) {

        super(parent);

        setTitleLabel();
        setDescriptionTextArea();

        setReportingUserLabel();
        setReportDateLabel();
        setTagsList();
        setImageButton();

        setAssignedDeveloperLabel();
        setResolutionDateLabel();
        setTypeLabel();
        setStatusLabel();
    }

    /**
     * Mostra il titolo dell'issue con un font grande ed evidente.
     */
    private void setTitleLabel() {

        JLabel titleLabel = new JLabel(IssueController.getInstance().getIssueTitle());
        titleLabel.setBorder(BorderFactory.createEmptyBorder());
        titleLabel.setBackground(ColorsList.EMPTY_COLOR);
        titleLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 24));

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(titleLabel);

        Constraints.setConstraints(0, 1, 3, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.LINE_START, 0.1f, 0.5f,
                new Insets(10, 60, 10, 0));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    /**
     * Configura l'area di testo per la descrizione.
     * <p>
     * Utilizza un {@link JTextArea} inserito in uno {@link JScrollPane}.
     * Importante: L'area è impostata come non editabile ({@code setEditable(false)}) poiché
     * questa è una vista di consultazione.
     * </p>
     */
    private void setDescriptionTextArea() {

        JTextArea descriptionTextArea = getJTextArea();

        JScrollPane tmpScrollPane = new JScrollPane(descriptionTextArea);

        tmpScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tmpScrollPane.setBackground(ColorsList.EMPTY_COLOR);
        tmpScrollPane.setViewportView(descriptionTextArea);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(tmpScrollPane);

        Constraints.setConstraints(0, 2, 4, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 60, 10, 60));
        mainPanel.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Metodo helper per creare e configurare la JTextArea.
     * Gestisce il caso di descrizione vuota fornendo un testo di default.
     */
    private static JTextArea getJTextArea() {

        String description = IssueController.getInstance().getIssueDescription().isEmpty() ?
                "Nessuna descrizione fornita per questa issue" : IssueController.getInstance().getIssueDescription();

        JTextArea descriptionTextArea = new JTextArea(description, 8, 40);

        descriptionTextArea.setBorder(BorderFactory.createEmptyBorder());
        descriptionTextArea.setBackground(Color.WHITE);
        descriptionTextArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        descriptionTextArea.setEditable(false); // Sola lettura
        descriptionTextArea.setFocusable(false);
        return descriptionTextArea;
    }

    /**
     * Mostra il tipo di issue (Bug, Feature, ecc.).
     */
    private void setTypeLabel() {
        JLabel typeLabel = new JLabel("Tipo: " + formatIssueType(IssueController.getInstance().getIssueType()));

        typeLabel.setBorder(BorderFactory.createEmptyBorder());
        typeLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(typeLabel);

        Constraints.setConstraints(0, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il pulsante per visualizzare i Tag.
     * <p>
     * Al click sul pulsante (icona tag):
     * 1. Controlla se ci sono tag.
     * 2. Se presenti, crea e mostra un {@link JPopupMenu} con la lista dei tag.
     * 3. Se assenti, mostra un {@link FloatingMessage} di avviso.
     * </p>
     */
    private void setTagsList() {

        IconButton tagsButton = new IconButton("/frontend/gui/images/tagsButton.svg", 32, 32);

        JPopupMenu menu = new JPopupMenu();

        for (String tag : IssueController.getInstance().getIssueTagsAsList()) {

            System.out.println(tag);
            JLabel tmpLabel = new JLabel(tag);
            tmpLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            menu.add(tmpLabel);
        }

        tagsButton.addActionListener(e -> {

            if (IssueController.getInstance().getIssueTagsAsList().getFirst().isEmpty())
                new FloatingMessage("Non ci sono etichette per questa issue",  tagsButton, FloatingMessage.WARNING_MESSAGE);
            else
                menu.show(tagsButton, 0, tagsButton.getHeight());
        });

        Constraints.setConstraints(2, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tagsButton, Constraints.getGridBagConstraints());
    }

    /**
     * Configura il pulsante per aprire l'allegato immagine.
     * <p>
     * Al click:
     * Tenta di aprire il file usando {@link Desktop#open(java.io.File)}.
     * Gestisce le eccezioni:
     * - {@code IOException}: Errore di apertura file.
     * - {@code NullPointerException}: Nessun file allegato (mostra avviso).
     * </p>
     */
    private void setImageButton() {

        IconButton imageButton = new IconButton("/frontend/gui/images/imageButton.svg", 32, 32);

        imageButton.addActionListener(e -> {

            try {
                Desktop.getDesktop().open(IssueController.getInstance().getIssueImageAsFile());
            } catch (IOException ex) {
                new FloatingMessage("Impossibile aprire il file.", imageButton, FloatingMessage.ERROR_MESSAGE);
            } catch (NullPointerException ex) {
                new FloatingMessage("Nessuna immagine è stata allegata per questa issue", imageButton, FloatingMessage.WARNING_MESSAGE);
            }
        });

        Constraints.setConstraints(3, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(imageButton, Constraints.getGridBagConstraints());
    }

    /**
     * Mostra lo stato corrente (To do, Assigned, Resolved).
     * <p>
     * Questo metodo è {@code protected} per permettere override o accessi dalle sottoclassi.
     * </p>
     */
    protected void setStatusLabel() {

        statusLabel = new JLabel("Stato: " + formatIssueStatus(IssueController.getInstance().getIssueStatus()));

        statusLabel.setBorder(BorderFactory.createEmptyBorder());
        statusLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(statusLabel);

        Constraints.setConstraints(1, 3, 1, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    /**
     * Mostra la data di creazione della segnalazione.
     */
    private void setReportDateLabel() {

        JLabel reportDateLabel = new JLabel("Segnalazione: " + formatDate(IssueController.getInstance().getIssueReportDate()));

        reportDateLabel.setBorder(BorderFactory.createEmptyBorder());
        reportDateLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(reportDateLabel);

        Constraints.setConstraints(0, 5, 2, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    /**
     * Mostra la data di risoluzione (se disponibile).
     * Gestisce il caso in cui la data sia null (issue non ancora risolta).
     */
    private void setResolutionDateLabel() {

        resolutionDateLabel = new JLabel("Risoluzione: " + (IssueController.getInstance().getIssueResolutionDate() != null ?
                formatDate(IssueController.getInstance().getIssueResolutionDate()) : "questa issue non è ancora stata risolta"));

        resolutionDateLabel.setBorder(BorderFactory.createEmptyBorder());
        resolutionDateLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(resolutionDateLabel);

        Constraints.setConstraints(2, 5, 2, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    /**
     * Utility per formattare la data nel formato italiano "dd/MM/yyyy".
     */
    private String formatDate(Date date) {

        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    /**
     * Mostra l'email dell'utente che ha creato la segnalazione.
     */
    private void setReportingUserLabel() {

        JLabel reportingUserLabel = new JLabel("Segnalatore: " + IssueController.getInstance().getIssueReportingUser().getEmail());

        reportingUserLabel.setBorder(BorderFactory.createEmptyBorder());
        reportingUserLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(reportingUserLabel);

        Constraints.setConstraints(0, 4, 2, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    /**
     * Mostra l'email dello sviluppatore assegnato.
     * Gestisce il caso in cui non ci sia ancora un assegnatario (null).
     */
    private void setAssignedDeveloperLabel() {

        assignedDeveloperLabel = new JLabel("Developer assegnato: " + ((IssueController.getInstance().getIssueAssignedDeveloper() != null) ?
                IssueController.getInstance().getIssueAssignedDeveloper().getEmail() : "questa issue non è ancora stata assegnata"));

        assignedDeveloperLabel.setBorder(BorderFactory.createEmptyBorder());
        assignedDeveloperLabel.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmp = ContainerFactory.createRoundedPanelContainer(assignedDeveloperLabel);

        Constraints.setConstraints(2, 4, 2, 1, GridBagConstraints.NONE,
                0, 0, GridBagConstraints.CENTER, 0.1f, 0.1f,
                new Insets(10, 10, 10, 10));
        mainPanel.add(tmp, Constraints.getGridBagConstraints());
    }

    /**
     * Metodo helper per convertire il valore enum del backend nell'etichetta del tipo (UI).
     * Es. "QUESTION" -> "Domanda".
     */
    protected String formatIssueType(String issueType) {

        return switch (issueType) {
            case "BUG" -> "bug";
            case "DOCUMENTATION" -> "documentazione";
            case "FEATURE" -> "feature";
            case "QUESTION" -> "domanda";
            default -> null;
        };
    }

    /**
     * Metodo helper per convertire l'etichetta dello stato (UI) nel valore enum del backend.
     * Es. "Risolte" -> "RESOLVED".
     */
    protected String formatIssueStatus(String issueStatus) {

        return switch (issueStatus) {
            case "ASSIGNED" -> "assegnata";
            case "RESOLVED" -> "risolta";
            case "TODO" -> "to do";
            default -> null;
        };
    }
}