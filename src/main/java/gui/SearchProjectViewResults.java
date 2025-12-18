package gui;

import javax.swing.*;
import java.awt.*;

public class SearchProjectViewResults {

    //todo
    /*
        deve contenere un JScrollPane che mostra la tabella costruita in SearchProjectResults,
        a inizio programma si potrebbe visualizzare un messaggio che suggerisce all'utente di effettuare una ricerca
        per visualizzare i risultati. Il JScrollPane deve essere contenuto in HomePanel della classe HomePanel.
     */
    private final JScrollPane scrollPane;

    public SearchProjectViewResults() {

        scrollPane = new JScrollPane();

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(ColorsList.EMPTY_COLOR);

        scrollPane.setViewportView(createTmpViewPanel());
    }

    private JPanel createTmpViewPanel() {

        RoundedPanel tmpViewPanel = new RoundedPanel(new BorderLayout());

        tmpViewPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpViewPanel.setBackground(Color.WHITE);

        tmpViewPanel.add(createTmpViewLabel());



        return tmpViewPanel;
    }

    private JLabel createTmpViewLabel() {

        JLabel tmpViewLabel = new JLabel("<html><center>Effettua una ricerca per visualizzare i progetti" +
                "<br>presenti e le azioni disponibili</center></html>");

        tmpViewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tmpViewLabel.setVerticalAlignment(SwingConstants.CENTER);

        tmpViewLabel.setBackground(ColorsList.EMPTY_COLOR);

        tmpViewLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));

        return tmpViewLabel;
    }

    public void updateViewportView (Component component) {

        scrollPane.setViewportView(component);
    }

    public JScrollPane getViewportScrollPane() {
        return scrollPane;
    }
}
