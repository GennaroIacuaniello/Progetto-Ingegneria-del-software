package frontend.gui;

import lombok.Getter;

import java.awt.*;

/**
 * Classe di utilità per la gestione semplificata dei {@link GridBagConstraints}.
 * <p>
 * Il layout {@link GridBagLayout} è potente ma molto verboso. Questa classe riduce il codice necessario
 * (boilerplate) mantenendo un'unica istanza statica di {@code GridBagConstraints} e fornendo metodi
 * helper (overloading) per impostarne tutti i parametri in una sola chiamata.
 * </p>
 * <p>
 * <strong>Pattern di utilizzo:</strong><br>
 * Si chiama uno dei metodi {@code setConstraints(...)} per configurare l'oggetto condiviso,
 * e subito dopo si recupera l'oggetto tramite {@code getGridBagConstraints()} per aggiungerlo al componente.
 * </p>
 */
public class Constraints {

    /**
     * L'istanza condivisa di {@link GridBagConstraints}.
     * <p>
     * Viene modificata dai metodi {@code setConstraints} e recuperata tramite il getter generato da Lombok
     * per essere passata al layout manager.
     * </p>
     */
    @Getter
    private static final GridBagConstraints gridBagConstraints;


    static {
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
    }

    /**
     * Imposta tutti i parametri dell'oggetto {@code GridBagConstraints} condiviso.
     *
     * @param gridx      La colonna della griglia in cui posizionare il componente.
     * @param gridy      La riga della griglia in cui posizionare il componente.
     * @param gridwidth  Il numero di colonne occupate dal componente.
     * @param gridheight Il numero di righe occupate dal componente.
     * @param fill       Come ridimensionare il componente se lo spazio è maggiore del necessario (es. {@code GridBagConstraints.BOTH}).
     * @param ipadx      Padding interno orizzontale.
     * @param ipady      Padding interno verticale.
     * @param anchor     Dove ancorare il componente se è più piccolo della cella (es. {@code GridBagConstraints.CENTER}).
     * @param weightx    Il peso orizzontale per la distribuzione dello spazio extra.
     * @param weighty    Il peso verticale per la distribuzione dello spazio extra.
     * @param insets     I margini esterni del componente.
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor, float weightx, float weighty, Insets insets) {

        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.gridwidth = gridwidth;
        gridBagConstraints.gridheight = gridheight;
        gridBagConstraints.fill = fill;
        gridBagConstraints.ipadx = ipadx;
        gridBagConstraints.ipady = ipady;
        gridBagConstraints.anchor = anchor;
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.weighty = weighty;
        gridBagConstraints.insets = insets;
    }

    /**
     * Imposta i vincoli utilizzando margini (Insets) nulli (0,0,0,0).
     *
     * @param gridx      La colonna della griglia.
     * @param gridy      La riga della griglia.
     * @param gridwidth  Numero di colonne occupate.
     * @param gridheight Numero di righe occupate.
     * @param fill       Comportamento di riempimento.
     * @param ipadx      Padding interno X.
     * @param ipady      Padding interno Y.
     * @param anchor     Ancoraggio.
     * @param weightx    Peso orizzontale.
     * @param weighty    Peso verticale.
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor, float weightx, float weighty) {

        Constraints.setConstraints(gridx, gridy, gridwidth, gridheight, fill,
                ipadx, ipady, anchor, weightx, weighty, new Insets(0, 0, 0, 0));
    }

    /**
     * Imposta i vincoli utilizzando i pesi di default (0.01f).
     *
     * @param gridx      La colonna della griglia.
     * @param gridy      La riga della griglia.
     * @param gridwidth  Numero di colonne occupate.
     * @param gridheight Numero di righe occupate.
     * @param fill       Comportamento di riempimento.
     * @param ipadx      Padding interno X.
     * @param ipady      Padding interno Y.
     * @param anchor     Ancoraggio.
     * @param insets     I margini esterni.
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor, Insets insets) {

        Constraints.setConstraints(gridx, gridy, gridwidth, gridheight, fill,
                ipadx, ipady, anchor, 0.01f, 0.01f, insets);

    }

    /**
     * Versione base: imposta i vincoli usando pesi di default e margini nulli.
     *
     * @param gridx      La colonna della griglia.
     * @param gridy      La riga della griglia.
     * @param gridwidth  Numero di colonne occupate.
     * @param gridheight Numero di righe occupate.
     * @param fill       Comportamento di riempimento.
     * @param ipadx      Padding interno X.
     * @param ipady      Padding interno Y.
     * @param anchor     Ancoraggio.
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor) {

        Constraints.setConstraints(gridx, gridy, gridwidth, gridheight, fill,
                ipadx, ipady, anchor, 0.01f, 0.01f, new Insets(0, 0, 0, 0));
    }
}