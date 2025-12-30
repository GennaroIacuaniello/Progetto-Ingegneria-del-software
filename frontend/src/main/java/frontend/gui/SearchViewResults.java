package frontend.gui;

import javax.swing.*;
import java.awt.*;

public class SearchViewResults {

    //todo
    /*
        deve contenere un JScrollPane che mostra la tabella costruita in SearchProjectResults,
        a inizio programma si potrebbe visualizzare un messaggio che suggerisce all'utente di effettuare una ricerca
        per visualizzare i risultati. Il JScrollPane deve essere contenuto in HomePanel della classe HomePanel.
     */
    private final JScrollPane scrollPane;
    private static final String TMPPANEL_PLACEHOLDER = "<html><center>Effettua una ricerca<br>" +
                                                       "per visualizzare i risultati<br>" +
                                                       "e le azioni disponibili</center></html>";

    public SearchViewResults() {

        scrollPane = new JScrollPane();

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(ColorsList.EMPTY_COLOR);

        scrollPane.setViewportView(createTmpViewPanel(TMPPANEL_PLACEHOLDER));
    }

    public SearchViewResults(String placeHolder) {

        scrollPane = new JScrollPane();

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(ColorsList.EMPTY_COLOR);

        scrollPane.setViewportView(createTmpViewPanel(placeHolder));
    }

    private JPanel createTmpViewPanel(String placeHolder) {

        RoundedPanel tmpViewPanel = new RoundedPanel(new GridBagLayout());

        tmpViewPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpViewPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 0, 1, 1, GridBagConstraints.BOTH,
                0, 0, GridBagConstraints.CENTER,
                new Insets(10, 10, 10, 10));
        tmpViewPanel.add(createTmpViewLabel(placeHolder), Constraints.getGridBagConstraints());

        return tmpViewPanel;
    }

    private JLabel createTmpViewLabel(String placeHolder) {

        JLabel tmpViewLabel = new JLabel(placeHolder);

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
