package obligatorio.obli.exceptions;

/**
 * Base class for all domain-specific HTTP exceptions in the toll system.
 * This is a CHECKED exception (extends Exception) to enforce explicit error
 * handling.
 *
 * All domain exceptions should extend this class and provide:
 * - User-friendly message (matching PRD exact wording)
 */
public class BaseHttpException extends Exception {
  public BaseHttpException(String message) {
    super(message);
  }

  public BaseHttpException(String message, Throwable cause) {
    super(message, cause);
  }
}
