package frontend.gui;

/**
 * RuntimeException thrown when invalid parameters are provided to FloatingMessage operations.
 * <p>
 * This exception is specifically designed to handle validation errors and configuration problems
 * within the {@link FloatingMessage} notification system. It serves as a dedicated exception
 * type for floating message operations, providing clear error identification and debugging
 * support for message display failures and configuration issues.
 * </p>
 * <p>
 * The FloatingMessageException is primarily used for:
 * </p>
 * <ul>
 *   <li><strong>Message Type Validation:</strong> Thrown when invalid message type constants are provided</li>
 *   <li><strong>Parameter Validation:</strong> Thrown when required parameters are null or invalid</li>
 *   <li><strong>Configuration Errors:</strong> Thrown when message configuration parameters are inconsistent</li>
 *   <li><strong>System Integrity:</strong> Ensures proper usage of the FloatingMessage API</li>
 * </ul>
 * <p>
 * The exception extends {@link RuntimeException}, making it an unchecked exception that does
 * not require explicit handling by calling code. This design choice allows the FloatingMessage
 * system to fail fast when improperly configured, while not burdening calling code with
 * mandatory exception handling for programming errors.
 * </p>
 * <p>
 * Common scenarios that trigger FloatingMessageException include:
 * </p>
 * <ul>
 *   <li><strong>Invalid Message Types:</strong> Using message type values other than ERROR_MESSAGE, WARNING_MESSAGE, or SUCCESS_MESSAGE</li>
 *   <li><strong>Null Parameters:</strong> Passing null values for required message content or triggering components</li>
 *   <li><strong>Configuration Conflicts:</strong> Providing conflicting or incompatible configuration parameters</li>
 *   <li><strong>System State Errors:</strong> Attempting operations when the system is in an invalid state</li>
 * </ul>
 * <p>
 * The exception provides descriptive error messages that aid in debugging and system
 * maintenance by clearly identifying the specific validation failure and providing guidance
 * for proper FloatingMessage usage. Error messages are designed to be informative for
 * developers while maintaining system security and stability.
 * </p>
 * <p>
 * Integration with the FloatingMessage system ensures that invalid operations are caught
 * early in the message creation process, preventing the display of malformed or improperly
 * configured notification messages that could compromise user experience or system stability.
 * </p>
 * <p>
 * The exception follows standard Java exception conventions, extending RuntimeException
 * and providing constructor overloads for message specification. This ensures compatibility
 * with standard exception handling patterns and logging frameworks throughout the application.
 * </p>
 * <p>
 * Usage within the airport management system includes validation of message parameters
 * during FloatingMessage construction, ensuring that all notification displays conform
 * to expected standards and provide consistent user experience across the application.
 * </p>
 * <p>
 * The exception is designed to be thrown immediately upon detection of invalid parameters,
 * providing fail-fast behavior that prevents the creation of invalid FloatingMessage
 * instances and ensures system integrity throughout notification operations.
 * </p>
 *
 * @author Aeroporto Di Napoli
 * @version 1.0
 * @since 1.0
 * @see FloatingMessage
 * @see RuntimeException
 * @see IllegalArgumentException
 */
public class FloatingMessageException extends RuntimeException {

    /**
     * Constructs a new FloatingMessageException with the specified detail message.
     * <p>
     * This constructor creates an exception instance with a descriptive error message
     * that explains the specific nature of the FloatingMessage validation failure.
     * The message provides detailed information about the invalid operation or parameter
     * that caused the exception, enabling effective debugging and system maintenance.
     * </p>
     * <p>
     * The message parameter should clearly describe the validation failure scenario
     * to help developers identify and resolve FloatingMessage configuration issues.
     * Common message formats include specific parameter validation failures, invalid
     * message type specifications, and configuration conflicts that prevent proper
     * message display.
     * </p>
     * <p>
     * Error messages are designed to be informative and specific, providing developers
     * with clear guidance about proper FloatingMessage usage and parameter requirements.
     * The messages include relevant context about expected values and parameter constraints
     * to facilitate quick resolution of configuration problems.
     * </p>
     * <p>
     * Examples of error messages include:
     * </p>
     * <ul>
     *   <li>"FloatingMessage: messageType must be one of FloatingMessage.ERROR_MESSAGE, FloatingMessage.WARNING_MESSAGE, FloatingMessage.SUCCESS_MESSAGE"</li>
     *   <li>"FloatingMessage: message content cannot be null or empty"</li>
     *   <li>"FloatingMessage: calling button parameter cannot be null"</li>
     *   <li>"FloatingMessage: invalid configuration parameters provided"</li>
     * </ul>
     * <p>
     * The constructor delegates to the parent RuntimeException constructor, ensuring
     * proper exception initialization and integration with standard Java exception
     * handling mechanisms. This provides compatibility with logging frameworks and
     * exception handling patterns throughout the application.
     * </p>
     * <p>
     * The message is preserved for later retrieval through the {@link #getMessage()}
     * method, enabling exception handlers and logging systems to access detailed
     * error information for debugging and system monitoring purposes.
     * </p>
     *
     * @param message the detail message explaining the specific FloatingMessage validation failure or configuration error
     */
    public FloatingMessageException(String message) {
        super(message);
    }
}