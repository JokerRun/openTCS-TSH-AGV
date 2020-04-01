/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams;

import static com.google.common.base.Ascii.ETX;
import static com.google.common.base.Ascii.STX;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.primitives.Ints;
import de.fraunhofer.iml.opentcs.example.common.telegrams.Response;
import static java.util.Objects.requireNonNull;

/**
 * Represents a vehicle status response sent from the vehicle.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public class StateResponse
    extends Response {

  /**
   * The id of the point at the vehicle's current position.
   */
  private int positionId;
  /**
   * The vehicle's operating state.
   */
  private OperatingState operatingState;
  /**
   * The vehicle's load state.
   */
  private LoadState loadState;
  /**
   * The id of the last received order.
   */
  private int lastReceivedOrderId;
  /**
   * The id of the current order.
   */
  private int currentOrderId;
  /**
   * The id of the last finished order.
   */
  private int lastFinishedOrderId;

  private int energyLevel;

  /**
   * Returns the id of the point at the vehicle's current position.
   *
   * @return The id of the point at the vehicle's current position
   */
  public int getPositionId() {
    return positionId;
  }

  /**
   * Returns the vehicle's operating state.
   *
   * @return The vehicle's operating state.
   */
  public OperatingState getOperatingState() {
    return operatingState;
  }

  /**
   * Returns the vehicle's load state.
   *
   * @return The vehicle's load state.
   */
  public LoadState getLoadState() {
    return loadState;
  }

  /**
   * Returns the id of the last received order.
   *
   * @return The id of the last received order.
   */
  public int getLastReceivedOrderId() {
    return lastReceivedOrderId;
  }

  /**
   * Returns the id of the current order.
   *
   * @return The id of the current order.
   */
  public int getCurrentOrderId() {
    return currentOrderId;
  }

  /**
   * Returns the id of the last finished order.
   *
   * @return The id of the last finished order.
   */
  public int getLastFinishedOrderId() {
    return lastFinishedOrderId;
  }
  @Override
  public String toString() {
    return "StateResponse{" + "id=" + id + '}';
  }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public void setOperatingState(OperatingState operatingState) {
        this.operatingState = operatingState;
    }

    public void setLoadState(LoadState loadState) {
        this.loadState = loadState;
    }

    public void setLastReceivedOrderId(int lastReceivedOrderId) {
        this.lastReceivedOrderId = lastReceivedOrderId;
    }

    public void setCurrentOrderId(int currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    public void setLastFinishedOrderId(int lastFinishedOrderId) {
        this.lastFinishedOrderId = lastFinishedOrderId;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    /**
   * The load handling state of a vehicle.
   */
  public static enum LoadState {
    /**
     * The vehicle's load handling state is currently empty.
     */
    EMPTY,
    /**
     * The vehicle's load handling state is currently full.
     */
    FULL,
    /**
     * The vehicle's load handling state is currently unknown.
     */
    UNKNOWN
  }

  /**
   * The operating state of a vehicle.
   */
  public static enum OperatingState {
    /**
     * The vehicle is currently executing an operation.
     */
    ACTING,
    /**
     * The vehicle is currently idle.
     */
    IDLE,
    /**
     * The vehicle is currently moving.
     */
    MOVING,
    /**
     * The vehicle is currently in an error state.
     */
    ERROR,
    /**
     * The vehicle is currently recharging.
     */
    CHARGING,
    /**
     * The vehicle's state is currently unknown.
     */
    UNKNOWN
  }
}
