package frontend.gui;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * Renderer grafico per celle di tabella contenenti icone.
 * <p>
 * Questa classe combina le funzionalità visive di {@link IconButton} con l'interfaccia standard
 * {@link TableCellRenderer}. Il suo scopo è solamente visualizzare l'icona all'interno della cella
 * e gestire correttamente il colore di sfondo (es. evidenziazione quando la riga è selezionata).
 * </p>
 * <p>
 * Nota: Questa classe si occupa della sola <i>visualizzazione</i>. L'interazione (click) è gestita
 * dai rispettivi {@code IconCellEditor}.
 * </p>
 */
class IconCellRenderer extends IconButton implements TableCellRenderer {

    /**
     * Costruttore del renderer.
     * <p>
     * Inizializza l'icona tramite la superclasse {@link IconButton} e configura le proprietà
     * di opacità necessarie per il rendering in tabella.
     * </p>
     *
     * @param url    Percorso dell'immagine/icona.
     * @param width  Larghezza desiderata.
     * @param height Altezza desiderata.
     */
    public IconCellRenderer(String url, int width, int height) {

        super(url, width, height);

        setBackGround();
    }

    /**
     * Configura il componente per supportare il disegno dello sfondo.
     * <p>
     * A differenza di un normale {@link IconButton} che è spesso trasparente per fondersi con il pannello,
     * il renderer deve essere opaco ({@code setOpaque(true)}) e avere l'area di contenuto riempita
     * ({@code setContentAreaFilled(true)}). Questo permette di visualizzare il colore di selezione
     * della riga della tabella dietro l'icona.
     * </p>
     */
    private void setBackGround() {

        this.setContentAreaFilled(true);
        this.setOpaque(true);
    }

    /**
     * Metodo standard di Swing per ottenere il componente "timbro" da disegnare nella cella.
     * <p>
     * Prima di restituire il componente (questo pulsante stesso), aggiorna il suo colore di sfondo:
     * <ul>
     * <li>Se la riga è selezionata, usa il colore di selezione della tabella (solitamente blu scuro).</li>
     * <li>Altrimenti, usa il colore di sfondo standard della tabella (solitamente bianco o alternato).</li>
     * </ul>
     * </p>
     *
     * @param table      La tabella che richiede il rendering.
     * @param value      Il valore della cella (non usato qui, poiché l'icona è fissa).
     * @param isSelected Indica se la cella/riga è attualmente selezionata.
     * @param hasFocus   Indica se la cella ha il focus (non usato).
     * @param row        L'indice della riga.
     * @param column     L'indice della colonna.
     * @return L'istanza corrente di {@code IconCellRenderer} configurata con i colori corretti.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected)
            setBackground(table.getSelectionBackground());
        else
            setBackground(table.getBackground());

        return this;
    }
}