package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Utility class for managing automated window disposal with fade-out animation effects.
 * <p>
 * This class provides timed disposal functionality for {@link JWindow} components, implementing
 * a sophisticated fade-out animation sequence that includes visibility delay, gradual opacity
 * reduction, and automatic window disposal. It is primarily designed for use with temporary
 * notification windows such as {@link FloatingMessage} components that require automatic
 * cleanup after a specified duration.
 * </p>
 * <p>
 * Timer coordination ensures smooth transitions between animation phases with proper resource
 * management throughout the disposal lifecycle. Each timer is configured with appropriate
 * repeat settings and cleanup procedures to prevent resource leaks and ensure complete
 * disposal completion.
 * </p>
 * <p>
 * Integration with {@link FloatingMessage} components enables automatic disposal of temporary
 * notification windows without requiring manual cleanup by calling code. This reduces
 * complexity in notification management while ensuring proper resource cleanup and
 * preventing window accumulation.
 * </p>
 * <p>
 * Performance considerations include minimal resource overhead through efficient timer
 * management and automatic cleanup procedures that prevent timer accumulation and
 * memory leaks during extended application operation.
 * </p>
 *
 * @author Aeroporto Di Napoli
 * @version 1.0
 * @since 1.0
 * @see JWindow
 * @see Timer
 * @see FloatingMessage
 * @see ActionListener
 */
public class DisposeTimers {

    /**
     * Timer responsible for gradual opacity reduction during the fade-out animation phase.
     * <p>
     * This timer executes at 10-millisecond intervals during the fade-out phase, reducing
     * the window's opacity by 0.01 per execution until the window reaches complete
     * transparency (opacity 0.0). The timer is configured with repeat enabled to support
     * continuous opacity reduction throughout the fade-out duration.
     * </p>
     * <p>
     * The timer's action listener implements conditional opacity reduction that checks
     * the current opacity level and applies appropriate reduction or sets final
     * transparency based on the current opacity value. This ensures smooth animation
     * progression regardless of the window's initial opacity setting.
     * </p>
     * <p>
     * Timer lifecycle management includes automatic termination when the fade-out
     * animation completes, with proper cleanup coordination through the disposal
     * timer to ensure complete resource deallocation.
     * </p>
     */
    private final Timer decreaseOpacityTimer;

    /**
     * Constructs a new DisposeTimers instance for managing automated window disposal with fade-out animation.
     * <p>
     * This constructor initializes the complete timed disposal system for the specified window,
     * creating and configuring all necessary timers for the three-phase disposal sequence.
     * The constructor immediately starts the disposal process, beginning with the visibility
     * phase and automatically progressing through fade-out and disposal phases.
     * </p>
     * <p>
     * The initialization process establishes:
     * </p>
     * <ul>
     *   <li><strong>Fade-out Timer:</strong> Configured for 10ms intervals with opacity reduction logic</li>
     *   <li><strong>Start Delay Timer:</strong> 1500ms single-execution timer to initiate fade-out phase</li>
     *   <li><strong>Disposal Timer:</strong> 2500ms single-execution timer for complete window cleanup</li>
     *   <li><strong>Timer Coordination:</strong> Proper sequencing and resource management between phases</li>
     * </ul>
     * <p>
     * The fade-out timer implements incremental opacity reduction logic that decreases
     * window opacity by 0.01 per execution cycle. The timer continues execution until
     * the window reaches complete transparency (opacity ≤ 0.01), at which point the
     * opacity is set to 0.0 for complete invisibility.
     * </p>
     * <p>
     * Start delay timer management provides the initial visibility duration (1500ms)
     * before beginning the fade-out animation. This timer executes once and automatically
     * initiates the fade-out phase by starting the opacity reduction timer while
     * stopping itself to prevent resource accumulation.
     * </p>
     * <p>
     * Disposal timer coordination ensures complete window cleanup after the total
     * disposal duration (2500ms). This timer handles final window disposal through
     * the dispose() method and performs comprehensive cleanup of all timer resources
     * including stopping active timers and disabling repeat functionality.
     * </p>
     * @param window the JWindow instance to be disposed with fade-out animation after the specified duration
     */
    public DisposeTimers(JWindow window) {

        decreaseOpacityTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(window.getOpacity() > 0.01f){
                    window.setOpacity(window.getOpacity()-0.01f);
                }
                else{
                    window.setOpacity(0.0f);
                }
            }
        });
        decreaseOpacityTimer.setRepeats(true);

        Timer startDecreaseOpacityTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Timer) e.getSource()).stop();
                decreaseOpacityTimer.start();
            }
        });
        startDecreaseOpacityTimer.setRepeats(false);

        Timer disposeTimer = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                ((Timer) e.getSource()).stop();
                decreaseOpacityTimer.setRepeats(false);
                decreaseOpacityTimer.stop();
            }
        });

        disposeTimer.start();
        startDecreaseOpacityTimer.start();
    }
}