package frontend.gui;

import frontend.exception.FloatingMessageException;

import javax.swing.*;
import java.awt.*;

/**
 * Classe che gestisce la visualizzazione di messaggi fluttuanti temporanei (Toast).
 * <p>
 * Questa classe crea una finestra senza bordi ({@link JWindow}) che appare in prossimità
 * di un componente specifico (solitamente un bottone) per fornire feedback immediato all'utente
 * (es. "Operazione riuscita", "Errore di connessione").
 * Il messaggio scompare automaticamente dopo un breve periodo con un effetto di dissolvenza,
 * gestito dalla classe di utilità {@link DisposeTimers}.
 * </p>
 */
public class FloatingMessage {

    /**
     * Costante per indicare un messaggio di errore (Stile Rosso).
     */
    public static final int ERROR_MESSAGE = 1;

    /**
     * Costante per indicare un messaggio di avviso (Stile Giallo).
     */
    public static final int WARNING_MESSAGE = 2;

    /**
     * Costante per indicare un messaggio di successo (Stile Verde).
     */
    public static final int SUCCESS_MESSAGE = 3;

    /**
     * La finestra senza decorazioni che funge da contenitore per il messaggio.
     */
    private JWindow messageWindow;

    /**
     * Il pannello con bordi arrotondati che contiene il testo e definisce il colore di sfondo.
     */
    private RoundedPanel messagePanel;

    /**
     * Costruttore: configura e mostra immediatamente il messaggio fluttuante.
     * <p>
     * Calcola la posizione, imposta lo stile in base al tipo e avvia il timer per la chiusura automatica.
     * </p>
     *
     * @param msg           Il testo del messaggio da visualizzare.
     * @param callingButton Il bottone che ha scatenato l'evento; il messaggio apparirà sopra di esso.
     * @param messageType   Il tipo di messaggio (ERROR, WARNING, SUCCESS) per determinare il colore.
     */
    public FloatingMessage (String msg, JButton callingButton, int messageType){

        setWindow(callingButton);
        setPanel(msg, messageType);

        // Avvia la gestione del ciclo di vita (attesa -> fade out -> dispose)
        new DisposeTimers(messageWindow);

        messageWindow.setVisible(true);
    }

    /**
     * Configura le proprietà della finestra (JWindow).
     * <p>
     * Imposta la trasparenza, le dimensioni e calcola la posizione sullo schermo
     * relativa al pulsante chiamante.
     * </p>
     *
     * @param callingButton Il bottone di riferimento per il posizionamento.
     */
    private void setWindow (JButton callingButton) {

        messageWindow = new JWindow();

        messageWindow.setAlwaysOnTop(true); // Assicura che il messaggio sia visibile sopra altre finestre
        messageWindow.setOpacity(0.75f);    // Imposta una leggera trasparenza
        messageWindow.setBackground(ColorsList.EMPTY_COLOR);

        JPanel contentPanel = (JPanel) messageWindow.getContentPane();
        contentPanel.setOpaque(false);
        messageWindow.setSize(300, 100);

        Point callingButtonLocation = new Point(callingButton.getLocationOnScreen());
        Point messageLocation = getPoint(callingButton, callingButtonLocation);

        messageWindow.setLocation(messageLocation);
    }

    /**
     * Calcola le coordinate (X, Y) dove posizionare il messaggio.
     * <p>
     * Tenta di centrare il messaggio orizzontalmente rispetto al bottone e di posizionarlo
     * verticalmente sopra di esso. Include controlli per evitare che il messaggio esca
     * dai bordi dello schermo (sinistra o destra).
     * </p>
     *
     * @param callingButton         Il bottone di riferimento.
     * @param callingButtonLocation La posizione del bottone sullo schermo.
     * @return Il punto (Point) in cui posizionare l'angolo in alto a sinistra della finestra del messaggio.
     */
    private Point getPoint(JButton callingButton, Point callingButtonLocation) {
        // Calcolo iniziale: centrato orizzontalmente rispetto al bottone, e sopra di esso (offset Y negativo)
        Point messageLocation = new Point((int) callingButtonLocation.getX() + (callingButton.getWidth() - messageWindow.getWidth()) / 2,
                (int) callingButtonLocation.getY() - messageWindow.getHeight() - 10);

        // Correzione bordi schermo
        if (messageLocation.getX() < 0)
            messageLocation.setLocation(5, (int) messageLocation.getY()); // Evita bordo sinistro
        else if (messageLocation.getX() + 300 > Toolkit.getDefaultToolkit().getScreenSize().getWidth())
            messageLocation.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 305),
                    (int) messageWindow.getLocationOnScreen().getY()); // Evita bordo destro (assumendo larghezza ~300)

        return messageLocation;
    }

    /**
     * Configura il pannello interno con il testo e i colori appropriati.
     *
     * @param msg         Il testo del messaggio (supporta HTML per formattazione).
     * @param messageType Il tipo di messaggio.
     */
    private void setPanel (String msg, int messageType) {

        messagePanel = new RoundedPanel(new BorderLayout());
        setColor(messageType);

        // Utilizza HTML per centrare il testo multilinea
        JLabel messageLabel = new JLabel("<html><center>" + msg + "</center></html>", SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLACK);

        messagePanel.add(messageLabel, BorderLayout.CENTER);

        messageWindow.add(messagePanel);
    }

    /**
     * Imposta i colori di sfondo e bordo del pannello in base al tipo di messaggio.
     *
     * @param messageType Il tipo di messaggio (ERROR, WARNING, SUCCESS).
     * @throws FloatingMessageException Se il messageType non è uno dei valori validi.
     */
    private void setColor(int messageType){

        switch (messageType) {
            case ERROR_MESSAGE -> {
                messagePanel.setBackground(ColorsList.RED_BACKGROUND_COLOR);
                messagePanel.setRoundBorderColor(ColorsList.RED_BORDER_COLOR);
            }
            case WARNING_MESSAGE -> {
                messagePanel.setBackground(ColorsList.YELLOW_BACKGROUND_COLOR);
                messagePanel.setRoundBorderColor(ColorsList.YELLOW_BORDER_COLOR);
            }
            case SUCCESS_MESSAGE -> {
                messagePanel.setBackground(ColorsList.GREEN_BACKGROUND_COLOR);
                messagePanel.setRoundBorderColor(ColorsList.GREEN_BORDER_COLOR);
            }
            default ->
                    throw new FloatingMessageException("FloatingMessage: messageType must be one of" +
                            "FloatingMessage.ERROR_MESSAGE, FloatingMessage.WARNING_MESSAGE, FloatingMessage.SUCCESS_MESSAGE");

        }
    }
}