package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Temporary notification display system for user feedback and operational status communication.
 * <p>
 * This class provides a sophisticated floating message system for displaying temporary notifications
 * positioned relative to triggering UI components. It implements automatic positioning, visual
 * styling based on message type, and automated disposal with fade-out animation effects for
 * optimal user experience and interface feedback.
 * </p>
 * <p>
 * The FloatingMessage class supports comprehensive notification functionality including:
 * </p>
 * <ul>
 *   <li><strong>Message Type Classification:</strong> Three distinct message types with appropriate visual styling</li>
 *   <li><strong>Intelligent Positioning:</strong> Automatic positioning relative to calling buttons with screen boundary detection</li>
 *   <li><strong>Visual Consistency:</strong> Rounded panel design with type-specific color schemes and transparency effects</li>
 *   <li><strong>Automatic Disposal:</strong> Timed disposal with fade-out animation through integrated DisposeTimers</li>
 *   <li><strong>Screen Boundary Handling:</strong> Automatic adjustment to prevent messages from appearing off-screen</li>
 * </ul>
 * <p>
 * The message type system provides three distinct categories for different operational scenarios:
 * </p>
 * <ul>
 *   <li><strong>Error Messages:</strong> Red-themed notifications for error conditions and validation failures</li>
 *   <li><strong>Warning Messages:</strong> Yellow-themed notifications for cautionary information and alerts</li>
 *   <li><strong>Success Messages:</strong> Green-themed notifications for successful operations and confirmations</li>
 * </ul>
 * <p>
 * Positioning intelligence ensures optimal message placement by calculating the ideal location
 * relative to the triggering button while implementing screen boundary detection to prevent
 * messages from appearing outside the visible screen area. The system automatically adjusts
 * horizontal positioning when messages would extend beyond screen boundaries.
 * </p>
 * <p>
 * Visual design incorporates semi-transparent overlays (75% opacity) with rounded panel styling
 * that maintains readability while providing subtle visual integration with the underlying
 * interface. Each message type features distinct color schemes including background colors
 * and border colors that provide immediate visual classification.
 * </p>
 * <p>
 * Automatic disposal functionality integrates seamlessly with the {@link DisposeTimers} system
 * to provide sophisticated fade-out animation sequences. Messages remain visible for an initial
 * period before gradually fading to transparency and complete disposal, ensuring user awareness
 * without requiring manual dismissal.
 * </p>
 * @author Aeroporto Di Napoli
 * @version 1.0
 * @since 1.0
 * @see DisposeTimers
 * @see RoundedPanel
 * @see FloatingMessageException
 * @see JWindow
 * @see JButton
 */
public class FloatingMessage {

    /**
     * Message type constant for error notifications and validation failures.
     * <p>
     * This constant identifies error messages that should be displayed with red-themed
     * styling to indicate problems, failures, or conditions requiring user attention.
     * Error messages typically appear for validation failures, operational errors,
     * and system exceptions that prevent successful task completion.
     * </p>
     */
    public static final int ERROR_MESSAGE = 1;

    /**
     * Message type constant for warning notifications and cautionary information.
     * <p>
     * This constant identifies warning messages that should be displayed with yellow-themed
     * styling to indicate cautionary conditions, alerts, or information requiring user
     * awareness. Warning messages typically appear for non-critical issues, operational
     * alerts, and conditions that may require user consideration.
     * </p>
     */
    public static final int WARNING_MESSAGE = 2;

    /**
     * Message type constant for success notifications and confirmation messages.
     * <p>
     * This constant identifies success messages that should be displayed with green-themed
     * styling to indicate successful operations, confirmations, and positive outcomes.
     * Success messages typically appear for completed operations, successful validations,
     * and confirmations of user actions.
     * </p>
     */
    public static final int SUCCESS_MESSAGE = 3;

    /**
     * Main window component for displaying the floating message notification.
     * <p>
     * This JWindow serves as the container for the message display, providing
     * always-on-top behavior, transparency effects, and precise positioning
     * relative to the triggering UI component. The window is configured with
     * semi-transparent background and appropriate sizing for message content.
     * </p>
     */
    private JWindow messageWindow;

    /**
     * Rounded panel component containing the message content and visual styling.
     * <p>
     * This RoundedPanel provides the visual container for message text with
     * rounded corners, type-specific background colors, and border styling.
     * The panel integrates with the message window to provide cohesive visual
     * presentation and proper content layout management.
     * </p>
     */
    private RoundedPanel messagePanel;

    /**
     * Constructs and displays a new floating message notification with automatic positioning and disposal.
     * <p>
     * This constructor creates a complete floating message system by initializing the message window,
     * configuring visual styling based on message type, positioning the message relative to the
     * calling button, and activating automatic disposal through the DisposeTimers system. The
     * message becomes immediately visible upon construction completion.
     * </p>
     * <p>
     * The construction process includes:
     * </p>
     * <ul>
     *   <li><strong>Window Initialization:</strong> Creating and configuring the JWindow with transparency and positioning</li>
     *   <li><strong>Panel Setup:</strong> Creating the RoundedPanel with message content and type-specific styling</li>
     *   <li><strong>Positioning Logic:</strong> Calculating optimal position relative to calling button with boundary detection</li>
     *   <li><strong>Disposal Integration:</strong> Activating DisposeTimers for automatic fade-out and cleanup</li>
     *   <li><strong>Visibility Activation:</strong> Making the message immediately visible to the user</li>
     * </ul>
     * @param msg the message text to display, supporting HTML formatting for rich text presentation
     * @param callingButton the JButton that triggered the message display, used for positioning calculations and screen coordinate determination
     * @param messageType the type of message determining visual styling, must be one of ERROR_MESSAGE, WARNING_MESSAGE, or SUCCESS_MESSAGE
     */
    public FloatingMessage (String msg, JButton callingButton, int messageType){

        setWindow(callingButton);
        setPanel(msg, messageType);


        new DisposeTimers(messageWindow);

        messageWindow.setVisible(true);
    }

    /**
     * Initializes and configures the message window with positioning and transparency settings.
     * <p>
     * This method creates and configures the JWindow that serves as the container for the
     * floating message, establishing window properties including transparency, size, positioning,
     * and always-on-top behavior. The method integrates intelligent positioning logic to
     * ensure optimal message placement relative to the triggering button.
     * </p>
     * <p>
     * Window configuration includes:
     * </p>
     * <ul>
     *   <li><strong>Always-on-Top Behavior:</strong> Ensures message visibility above other application windows</li>
     *   <li><strong>Transparency Settings:</strong> 75% opacity for subtle visual integration</li>
     *   <li><strong>Background Configuration:</strong> Transparent background for rounded panel integration</li>
     *   <li><strong>Content Panel Setup:</strong> Transparent content panel for proper visual composition</li>
     *   <li><strong>Size Configuration:</strong> Standard 300x100 pixel dimensions for consistent message sizing</li>
     *   <li><strong>Position Calculation:</strong> Intelligent positioning relative to calling button with boundary detection</li>
     * </ul>
     * @param callingButton the JButton component that triggered the message display, used for position calculations and screen coordinate determination
     */
    private void setWindow (JButton callingButton) {

        messageWindow = new JWindow();

        messageWindow.setAlwaysOnTop(true);
        messageWindow.setOpacity(0.75f);
        messageWindow.setBackground(new Color(0, 0, 0, 0));

        JPanel contentPanel = (JPanel) messageWindow.getContentPane();
        contentPanel.setOpaque(false);
        messageWindow.setSize(300, 100);

        Point callingButtonLocation = new Point(callingButton.getLocationOnScreen());
        Point messageLocation = getPoint(callingButton, callingButtonLocation);

        messageWindow.setLocation(messageLocation);
    }

    /**
     * Calculates optimal message position with intelligent screen boundary detection and adjustment.
     * <p>
     * This method implements sophisticated positioning logic to determine the ideal location
     * for displaying floating messages relative to the triggering button while ensuring
     * complete visibility within screen boundaries. The method provides automatic adjustment
     * for horizontal positioning when messages would extend beyond screen edges.
     * </p>
     * <p>
     * The positioning algorithm includes:
     * </p>
     * <ul>
     *   <li><strong>Centered Horizontal Alignment:</strong> Centers message horizontally relative to triggering button</li>
     *   <li><strong>Above-Button Vertical Positioning:</strong> Positions message above button with appropriate spacing</li>
     *   <li><strong>Left Boundary Detection:</strong> Prevents messages from extending beyond left screen edge</li>
     *   <li><strong>Right Boundary Detection:</strong> Prevents messages from extending beyond right screen edge</li>
     *   <li><strong>Automatic Adjustment:</strong> Repositions messages when boundary violations are detected</li>
     * </ul>
     * @param callingButton the JButton component used for width calculations and positioning reference
     * @param callingButtonLocation the screen coordinates of the triggering button for position calculations
     * @return the calculated Point representing the optimal screen position for the message window
     */
    private Point getPoint(JButton callingButton, Point callingButtonLocation) {
        Point messageLocation = new Point((int) callingButtonLocation.getX() + (callingButton.getWidth() - messageWindow.getWidth()) / 2,
                (int) callingButtonLocation.getY() - messageWindow.getHeight() - 10);

        if (messageLocation.getX() < 0)
            messageLocation.setLocation(5, (int) messageLocation.getY());
        else if (messageLocation.getX() + 300 > Toolkit.getDefaultToolkit().getScreenSize().getWidth())
            messageLocation.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 305),
                    (int) messageWindow.getLocationOnScreen().getY());
        return messageLocation;
    }

    /**
     * Creates and configures the message content panel with HTML text formatting and type-specific styling.
     * <p>
     * This method establishes the visual content container for the floating message by creating
     * a RoundedPanel with BorderLayout, configuring message text with HTML formatting for
     * centered display, and applying type-specific visual styling through color configuration.
     * The method integrates text presentation with visual design for optimal user readability.
     * </p>
     * <p>
     * Panel configuration includes:
     * </p>
     * <ul>
     *   <li><strong>RoundedPanel Creation:</strong> Creates styled panel container with BorderLayout for content organization</li>
     *   <li><strong>Color Styling:</strong> Applies message type-specific background and border colors</li>
     *   <li><strong>Label Configuration:</strong> Creates centered JLabel with black text for optimal readability</li>
     *   <li><strong>Layout Integration:</strong> Integrates content label with panel layout and window structure</li>
     * </ul>
     * @param msg the message text content to display, which will be formatted with HTML for centered presentation
     * @param messageType the message type constant determining visual styling and color scheme application
     */
    private void setPanel (String msg, int messageType) {

        messagePanel = new RoundedPanel(new BorderLayout());
        setColor(messageType);

        JLabel messageLabel = new JLabel("<html><center>" + msg + "</center></html>", SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLACK);

        messagePanel.add(messageLabel, BorderLayout.CENTER);

        messageWindow.add(messagePanel);
    }

    /**
     * Applies message type-specific color schemes to the message panel for visual classification.
     * <p>
     * This method implements the visual styling system by applying appropriate background and
     * border colors based on the message type parameter. Each message type receives distinct
     * color treatment that provides immediate visual classification and semantic meaning for
     * users, enhancing the communication effectiveness of the notification system.
     * </p>
     * <p>
     * Color scheme implementation includes:
     * </p>
     * <ul>
     *   <li><strong>Error Messages:</strong> Red background (200, 60, 60) with dark red border (120, 0, 10)</li>
     *   <li><strong>Warning Messages:</strong> Yellow background (240, 220, 50) with dark yellow border (160, 140, 10)</li>
     *   <li><strong>Success Messages:</strong> Green background (139, 255, 104) with dark green border (55, 142, 5)</li>
     *   <li><strong>Type Validation:</strong> Comprehensive validation with exception throwing for invalid message types</li>
     * </ul>
     *
     * @param messageType the message type constant that determines color scheme application, must be ERROR_MESSAGE, WARNING_MESSAGE, or SUCCESS_MESSAGE
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