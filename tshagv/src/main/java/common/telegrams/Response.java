/**
 * Copyright (c) Fraunhofer IML
 */
package common.telegrams;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

/**
 * A response represents an answer of a vehicle control to a request sent by the control system.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public abstract class Response
    extends Telegram {


    /**
   * Checks whether this is a response to the given request.
   * <p>
   * This implementation only checks for matching telegram ids.
   * Subclasses may want to extend this check.
   * </p>
   *
   * @param request The request to check with.
   * @return {@code true} if, and only if, the given request's id matches this response's id.
   */
  public boolean isResponseTo(@Nonnull Request request) {
    requireNonNull(request, "request");
    return request.getId() == getId();
  }

}
