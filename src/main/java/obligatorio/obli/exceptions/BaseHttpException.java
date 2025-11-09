package obligatorio.obli.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Base class for all domain-specific HTTP exceptions in the toll system.
 * This is a CHECKED exception (extends Exception) to enforce explicit error
 * handling.
 *
 * All domain exceptions should extend this class and provide:
 * - User-friendly message (matching PRD exact wording)
 * - Error code for API consumers
 * - HTTP status code for REST responses
 */
public class BaseHttpException extends Exception {
  private final String errorCode;
  private final HttpStatus statusCode;

  public BaseHttpException(String message, String errorCode, HttpStatus statusCode) {
    super(message);
    this.errorCode = errorCode;
    this.statusCode = statusCode;
  }

  public BaseHttpException(String message, String errorCode, HttpStatus statusCode, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
    this.statusCode = statusCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }
}
