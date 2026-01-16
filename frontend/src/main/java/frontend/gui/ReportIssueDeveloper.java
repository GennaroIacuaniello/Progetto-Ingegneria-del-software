package frontend.gui;

import frontend.controller.IssueController;
import frontend.dto.IssueDTO;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Pannello per la segnalazione di nuove issue, specifico per il ruolo Sviluppatore.
 * <p>
 * Questa classe estende {@link ReportIssueUser} per ereditare la struttura base del form
 * (Titolo, Descrizione, Tipo, Tag, Allegati).
 * La differenza sostanziale risiede nell'aggiunta del campo <b>Priorità</b>.
 * Mentre un utente base non ha le competenze o i permessi per definire l'urgenza di un bug,
 * lo sviluppatore può impostare immediatamente il livello di priorità (es. Alta, Critica)
 * durante la creazione della segnalazione.
 * </p>
 */
public class ReportIssueDeveloper extends ReportIssueUser {

    /**
     * Menu a tendina per la selezione della priorità iniziale.
     */
    private JComboBox<String> priorityComboBox;

    /**
     * Opzioni di priorità disponibili.
     */
    private static final String[] options = {"Molto bassa", "Bassa", "Media", "Alta", "Molto alta"};

    /**
     * Costruttore del pannello di segnalazione Developer.
     * <p>
     * Inizializza il pannello richiamando il costruttore della superclasse e forza un aggiornamento
     * grafico per assicurare che i nuovi componenti siano renderizzati correttamente.
     * </p>
     *
     * @param mainFrame     Il frame principale dell'applicazione.
     * @param homePanelUser Il pannello Home per la navigazione.
     */
    public ReportIssueDeveloper(JFrame mainFrame, HomePanelUser homePanelUser) {

        super(mainFrame,  homePanelUser);

        revalidate();
        repaint();
    }

    /**
     * Configura i componenti dell'interfaccia.
     * <p>
     * Sovrascrive il metodo della superclasse per iniettare il componente {@code setPriorityComboBox()}
     * nella procedura di costruzione della UI.
     * </p>
     */
    @Override
    protected void setComponents(JFrame mainFrame, HomePanelUser homePanelUser) {

        setBackButton(homePanelUser);
        setTitleTextField();
        setDescriptionTextArea();
        setTypeComboBox();
        setTagsButton(mainFrame);
        setFileChooserPanel();
        setPriorityComboBox(); // Componente aggiuntivo specifico
        setReportButton(homePanelUser);
    }

    /**
     * Esegue l'invio della segnalazione al sistema.
     * <p>
     * Sovrascrive il metodo di report per includere la priorità nel DTO.
     * 1. Crea un oggetto {@link IssueDTO}.<br>
     * 2. Popola i campi standard (Titolo, Descrizione, Tipo).<br>
     * 3. <b>Nuovo:</b> Converte la stringa della priorità selezionata in intero tramite il Controller e la setta nel DTO.<br>
     * 4. Invia la richiesta al backend tramite {@link IssueController#reportIssue}.<br>
     * 5. Se successo, torna alla Home.
     * </p>
     */
    @Override
    protected void report(HomePanelUser homePanelUser) {

        IssueDTO issue = new IssueDTO();

        issue.setTitle(titleTextField.getText());
        issue.setDescription((descriptionTextArea.getText().equals(DESCRIPTION_PLACEHOLDER) ? "" : descriptionTextArea.getText()));
        issue.setTypeWithString(formatIssueType(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()));

        // Gestione specifica della priorità
        issue.setPriority(IssueController.getInstance().priorityStringToInt(Objects.requireNonNull(priorityComboBox.getSelectedItem()).toString()));

        boolean success = IssueController.getInstance().reportIssue(issue, tagsButton.getTags(), fileChooserPanel.getSelectedFile());

        if(!success)
            return;

        homePanelUser.returnToDefaultContentPanel();
    }

    /**
     * Inizializza e posiziona il menu a tendina per la priorità.
     * <p>
     * Crea il JComboBox, lo stilizza (rimuovendo i bordi) e lo avvolge in un contenitore arrotondato.
     * Lo posiziona nella griglia del layout (riga 4).
     * </p>
     */
    private void setPriorityComboBox() {

        priorityComboBox = new JComboBox<>(options);
        priorityComboBox.setBorder(BorderFactory.createEmptyBorder());
        priorityComboBox.setBackground(ColorsList.EMPTY_COLOR);

        RoundedPanel tmpPanel = ContainerFactory.createRoundedPanelContainer(priorityComboBox);

        Constraints.setConstraints(2, 4, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_END,
                0.5f, 0.5f);
        this.add(createTransparentLabel("Priorità: "), Constraints.getGridBagConstraints());

        Constraints.setConstraints(3, 4, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.LINE_START,
                0.5f, 0.5f);
        this.add(tmpPanel, Constraints.getGridBagConstraints());
    }

    /**
     * Sovrascrive il posizionamento del pulsante Tag.
     * <p>
     * Necessario per adattare il layout alla presenza del campo Priorità aggiuntivo.
     * </p>
     */
    @Override
    protected void setTagsButtonConstraints() {

        Constraints.setConstraints(1, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
    }

    /**
     * Sovrascrive il posizionamento del pannello di selezione file.
     * <p>
     * Necessario per adattare il layout alla presenza del campo Priorità aggiuntivo.
     * </p>
     */
    @Override
    protected void setFileChooserPanelConstraints() {

        Constraints.setConstraints(2, 3, 1, 1,
                GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER,
                0.5f, 0.5f);
    }
}