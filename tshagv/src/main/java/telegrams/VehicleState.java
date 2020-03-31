package telegrams; /**
 * Copyright (c) Fraunhofer IML
 */

import com.alibaba.fastjson.JSONObject;
import telegrams.OrderResponse;
import telegrams.StateResponse;

/**
 * Represents the state of a physical vehicle.
 * Generally the content matches a {@link StateResponse}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class VehicleState {

  /**
   * The telegram counter to match request and response.
   */
  private int telegramCounter;

  /**
   * The current operation mode of the vehicle.
   * (M)oving, (A)cting, (I)dle, (C)harging, (E)rror.
   */
  private OperatingState operationMode = OperatingState.IDLE;
  /**
   * The load handling state of the vehicle.
   * (E)mpty, (F)ull, (U)nknown.
   */
  private LoadState loadState = LoadState.EMPTY;
  /**
   * The id of the current position.
   */
  private int positionId;
  /**
   * The order id of the last finished order.
   */
  private int lastFinishedOrderId;
  /**
   * The order id of the currently executed order.
   */
  private int currOrderId;
  /**
   * The order id of the last received oder.
   */
  private int lastReceivedOrderId;

  public int getTelegramCounter() {
    return telegramCounter;
  }

  public void setTelegramCounter(int telegramCounter) {
    this.telegramCounter = telegramCounter;
  }

    public OperatingState getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(OperatingState operationMode) {
        this.operationMode = operationMode;
    }

    public LoadState getLoadState() {
        return loadState;
    }

    public void setLoadState(LoadState loadState) {
        this.loadState = loadState;
    }

    public int getPositionId() {
    return positionId;
  }

  public void setPositionId(int positionId) {
    this.positionId = positionId;
  }

  public int getLastFinishedOrderId() {
    return lastFinishedOrderId;
  }

  public void setLastFinishedOrderId(int lastFinishedOrderId) {
    this.lastFinishedOrderId = lastFinishedOrderId;
  }

  public int getCurrOrderId() {
    return currOrderId;
  }

  public void setCurrOrderId(int currOrderId) {
    this.currOrderId = currOrderId;
  }

  public int getLastReceivedOrderId() {
    return lastReceivedOrderId;
  }

  public void setLastReceivedOrderId(int lastReceivedOrderId) {
    this.lastReceivedOrderId = lastReceivedOrderId;
  }

  /**
   * Creates a state response for the current vehicle state
   *
   * @return A state response
   */
  public String toStateResponse() {

      String jsonString = JSONObject.toJSONString(this);
      return jsonString;
  }

  /**
   * Creates an order response for the current vehicle state.
   *
   * @return The order response
   */
  public String toOrderResponse() {

      OrderResponse orderResponse = new OrderResponse();
      orderResponse.setOrderId(lastReceivedOrderId);
      String jsonString = JSONObject.toJSONString(orderResponse);
      return jsonString;
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
    public enum OperatingState {
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
