package frontend.gui;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Classe di utilità per la gestione dell'animazione di chiusura delle finestre temporanee.
 * <p>
 * Questa classe viene istanziata per gestire il ciclo di vita di una {@link JWindow} (tipicamente un messaggio fluttuante).
 * Esegue una sequenza temporale predefinita: mantiene la finestra visibile per un breve periodo,
 * avvia un'animazione di dissolvenza (fade-out) riducendo progressivamente l'opacità,
 * e infine chiude (dispose) la finestra liberando le risorse.
 * </p>
 */
public class DisposeTimers {

    /**
     * Timer responsabile dell'aggiornamento grafico per l'effetto di dissolvenza.
     * <p>
     * Viene eseguito a intervalli brevi (10ms) per ridurre l'opacità della finestra
     * di piccoli step (0.01f) fino a renderla invisibile.
     * </p>
     */
    private final Timer decreaseOpacityTimer;

    /**
     * Costruttore che inizializza e avvia la sequenza di chiusura automatica.
     * <p>
     * La sequenza temporale è la seguente:
     * <ol>
     * <li><b>Attesa iniziale:</b> La finestra rimane pienamente visibile per 1500ms.</li>
     * <li><b>Dissolvenza:</b> Dopo 1500ms, parte {@code decreaseOpacityTimer} che riduce l'opacità dell'1% ogni 10ms (durata totale dissolvenza: 1 secondo).</li>
     * <li><b>Chiusura:</b> Dopo 2500ms totali (1500ms attesa + 1000ms dissolvenza), la finestra viene chiusa definitivamente.</li>
     * </ol>
     * </p>
     *
     * @param window La finestra {@link JWindow} su cui applicare l'animazione e la chiusura.
     */
    public DisposeTimers(JWindow window) {

        // 1. Configurazione del timer per l'animazione di dissolvenza (ogni 10ms)
        decreaseOpacityTimer = new Timer(10, e -> {
            if(window.getOpacity() > 0.01f){
                // Riduce l'opacità corrente dello 0.01
                window.setOpacity(window.getOpacity()-0.01f);
            }
            else{
                // Opacità a 0 se siamo vicini al limite
                window.setOpacity(0.0f);
            }
        });
        decreaseOpacityTimer.setRepeats(true);

        // 2. Timer che fa partire la dissolvenza dopo 1.5 secondi
        Timer startDecreaseOpacityTimer = new Timer(1500, e -> {
            ((Timer) e.getSource()).stop(); // Ferma se stesso dopo la prima esecuzione
            decreaseOpacityTimer.start();   // Avvia l'animazione
        });
        startDecreaseOpacityTimer.setRepeats(false);

        // 3. Timer "killer" che chiude la finestra dopo 2.5 secondi totali
        Timer disposeTimer = new Timer(2500, e -> {
            window.dispose();                 // Chiude la finestra
            ((Timer) e.getSource()).stop();   // Ferma se stesso
            decreaseOpacityTimer.setRepeats(false);
            decreaseOpacityTimer.stop();      // Assicura che l'animazione si fermi
        });

        // Avvio della sequenza
        disposeTimer.start();
        startDecreaseOpacityTimer.start();
    }
}