/**
 * Copyright (c) Fraunhofer IML
 */
package telegrams;


import common.telegrams.Response;

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
    private VehicleState.OperatingState operatingState;
    /**
     * The vehicle's load state.
     */
    private VehicleState.LoadState loadState;
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
    public VehicleState.OperatingState getOperatingState() {
        return operatingState;
    }

    /**
     * Returns the vehicle's load state.
     *
     * @return The vehicle's load state.
     */
    public VehicleState.LoadState getLoadState() {
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

    public void setOperatingState(VehicleState.OperatingState operatingState) {
        this.operatingState = operatingState;
    }

    public void setLoadState(VehicleState.LoadState loadState) {
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

}
