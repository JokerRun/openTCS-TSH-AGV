/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.common.telegrams;

import java.io.Serializable;
import static java.util.Objects.requireNonNull;

/**
 * The base class for all telegram types used for communication with the vehicle.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public abstract class Telegram
    implements Serializable {

  /**
   * The default value for a telegram's id.
   */
  public static final int ID_DEFAULT = 0;
  /**
   * The identifier for a specific telegram instance.
   */
  protected int id;

  /**
   * Returns the identifier for this specific telegram instance.
   *
   * @return The identifier for this specific telegram instance.
   */
  public int getId() {
    return id;
  }

    public void setId(int id) {
        this.id = id;
    }
}
