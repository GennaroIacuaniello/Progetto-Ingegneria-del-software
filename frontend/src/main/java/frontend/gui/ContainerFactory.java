package frontend.gui;

import java.awt.*;

/**
 * Factory per la creazione di container grafici standardizzati.
 * <p>
 * Questa classe fornisce metodi statici per incapsulare componenti grafici (es. tabelle, form, liste)
 * all'interno di pannelli decorativi standard (es. con bordi arrotondati e padding),
 * garantendo un aspetto coerente in tutta l'interfaccia utente senza duplicazione di codice.
 * </p>
 */
public class ContainerFactory {

    /**
     * Costruttore privato.
     */
    private ContainerFactory(){

    }
    /**
     * Avvolge un componente generico all'interno di un pannello con bordi arrotondati.
     * <p>
     * Il metodo crea un {@link RoundedPanel}, imposta lo sfondo bianco e il colore del bordo
     * definito in {@link ColorsList}. Il componente passato come argomento viene aggiunto al pannello
     * utilizzando un {@link GridBagLayout} configurato per espandersi e riempire lo spazio disponibile,
     * mantenendo un margine (padding) interno di 10 pixel su tutti i lati.
     * </p>
     *
     * @param component Il componente grafico (es. JTable, JPanel interno) da inserire nel container.
     * @return Un'istanza di {@link RoundedPanel} contenente il componente passato.
     */
    public static RoundedPanel createRoundedPanelContainer(Component component) {

        RoundedPanel tmpPanel = new RoundedPanel(new GridBagLayout());

        tmpPanel.setRoundBorderColor(ColorsList.BORDER_COLOR);
        tmpPanel.setBackground(Color.WHITE);

        Constraints.setConstraints(0, 0, 1, 1,
                GridBagConstraints.BOTH, 0, 0, GridBagConstraints.CENTER,
                1f, 1f, new Insets(10, 10, 10, 10));
        tmpPanel.add(component, Constraints.getGridBagConstraints());

        return tmpPanel;
    }
}