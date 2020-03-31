/**
 * Copyright (c) Fraunhofer IML
 */
package telegrams;


import common.telegrams.Request;

/**
 * Represents a state request addressed to the vehicle.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class StateRequest
    extends Request {

  @Override
  public void updateRequestContent(int telegramId) {
    id = telegramId;
  }

}
