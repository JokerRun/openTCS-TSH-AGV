/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams;

import static com.google.common.base.Ascii.ETX;
import static com.google.common.base.Ascii.STX;
import com.google.common.primitives.Ints;
import de.fraunhofer.iml.opentcs.example.common.telegrams.Request;

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
