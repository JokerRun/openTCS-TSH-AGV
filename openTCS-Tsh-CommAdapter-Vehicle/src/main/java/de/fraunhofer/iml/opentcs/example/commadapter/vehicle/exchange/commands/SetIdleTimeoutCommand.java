/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands;

import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.TshCommAdapter;
import org.opentcs.drivers.vehicle.AdapterCommand;
import org.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to set the adapters's idle timeout.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetIdleTimeoutCommand
    implements AdapterCommand {

  /**
   * The idle timeout to set.
   */
  private final int timeout;

  /**
   * Creates a new instance.
   *
   * @param timeout The idle timeout to set.
   */
  public SetIdleTimeoutCommand(int timeout) {
    this.timeout = timeout;
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    if (!(adapter instanceof TshCommAdapter)) {
      return;
    }

    TshCommAdapter exampleAdapter = (TshCommAdapter) adapter;
    exampleAdapter.getProcessModel().setVehicleIdleTimeout(timeout);
  }
}
