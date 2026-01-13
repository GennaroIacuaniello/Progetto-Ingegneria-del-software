package frontend.gui;

import java.awt.*;

/**
 * Classe di utilità che contiene la definizione dei colori utilizzati nell'interfaccia grafica.
 * <p>
 * Centralizza la palette cromatica dell'applicazione, garantendo coerenza stilistica tra i vari componenti
 * (pulsanti, sfondi, bordi, tabelle, ecc.). I colori sono definiti come costanti statiche pubbliche.
 * </p>
 */
public class ColorsList {

    /**
     * Costruttore privato per SQ.
     */
    private ColorsList(){

    }
    /**
     * Colore completamente trasparente (Alpha = 0).
     */
    public static final Color EMPTY_COLOR = new Color (0, 0, 0, 0);

    /**
     * Colore principale per i bordi degli elementi attivi o evidenziati (Blu scuro/vivido).
     */
    public static final Color BORDER_COLOR = new Color (77, 133, 255);

    /**
     * Colore di sfondo per i frame o pannelli principali (Blu molto chiaro).
     */
    public static final Color FRAME_COLOR = new Color(204, 229, 255);

    /**
     * Colore di sfondo generale per le aree di contenuto (Bianco ghiaccio/Blu pallido).
     */
    public static final Color BACKGROUND_COLOR = new Color(230, 238, 255);

    /**
     * Colore del bordo per elementi di successo o positivi (Verde scuro).
     */
    public static final Color GREEN_BORDER_COLOR = new Color (55, 142, 5);

    /**
     * Colore di sfondo per elementi di successo (Verde chiaro).
     */
    public static final Color GREEN_BACKGROUND_COLOR = new Color (139, 255, 104);

    /**
     * Colore del bordo per elementi di errore o critici (Rosso scuro).
     */
    public static final Color RED_BORDER_COLOR = new Color (120, 0, 10);

    /**
     * Colore di sfondo per elementi di errore (Rosso chiaro).
     */
    public static final Color RED_BACKGROUND_COLOR = new Color (200, 60, 60);

    /**
     * Colore del bordo per avvisi o stati intermedi (Giallo scuro/Ocra).
     */
    public static final Color YELLOW_BORDER_COLOR = new Color (160, 140, 10);

    /**
     * Colore di sfondo per avvisi (Giallo acceso).
     */
    public static final Color YELLOW_BACKGROUND_COLOR = new Color (240, 220, 50);

    /**
     * Colore di sfondo per l'intestazione delle tabelle (Simile al FRAME_COLOR).
     */
    public static final Color TABLE_HEADER_BACKGROUND_COLOR = new Color (204, 229, 255);

    /**
     * Colore di sfondo per i pannelli dei titoli (Blu medio).
     */
    public static final Color TITLE_BACKGROUND_COLOR = new Color(153, 201, 255);

    /**
     * Colore primario dell'applicazione, usato per testi importanti o pulsanti principali (Blu standard).
     */
    public static final Color PRIMARY_COLOR = new Color(0, 120, 215);

    /**
     * Colore standard per il testo (Grigio scuro quasi nero).
     */
    public static final Color TEXT_COLOR = new Color(50, 50, 50);
}