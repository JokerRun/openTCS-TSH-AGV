/**
 * Copyright (c) Fraunhofer IML
 */
package telegrams;


import common.telegrams.Request;

import static java.util.Objects.requireNonNull;

/**
 * Represents an order request addressed to the vehicle.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public class OrderRequest
    extends Request {

  /**
   * The request type.
   */
  public static final byte TYPE = 2;
  /**
   * The expected length of a telegram of this type.
   */
  public static final int TELEGRAM_LENGTH = 12;
  /**
   * The size of the payload (the raw content, without STX, SIZE, CHECKSUM and ETX).
   */
  public static final int PAYLOAD_LENGTH = TELEGRAM_LENGTH - 4;
  /**
   * The position of the checksum byte.
   */
  public static final int CHECKSUM_POS = TELEGRAM_LENGTH - 2;
  /**
   * The transport order orderId.
   */
  private final int orderId;
  /**
   * The name of the destination point.
   */
  private final int destinationId;
  /**
   * The action to execute at the destination point.
   */
  private final OrderAction destinationAction;


  /**
   * Creates a new instance.
   *
   * @param telegramId The request's telegram id.
   * @param orderId The order id.
   * @param destinationId The name of the destination point.
   * @param destinationAction The action to execute at the destination point.
   */
  public OrderRequest(int telegramId,
                      int orderId,
                      int destinationId,
                      OrderAction destinationAction) {
    super();
    this.id = telegramId;
    this.orderId = orderId;
    this.destinationId = destinationId;
    this.destinationAction = requireNonNull(destinationAction, "destinationAction");
  }

  /**
   * Returns this telegram's order orderId.
   *
   * @return This telegram's order orderId
   */
  public int getOrderId() {
    return orderId;
  }

  /**
   * Returns this telegram's destination name.
   *
   * @return This telegram's destination name
   */
  public int getDestinationId() {
    return destinationId;
  }

  /**
   * Returns this telegram's destination action.
   *
   * @return This telegram's destination action
   */
  public OrderAction getDestinationAction() {
    return destinationAction;
  }

  @Override
  public String toString() {
    return "OrderRequest{" + "orderId=" + orderId + ","
        + " destinationId=" + destinationId + ","
        + " destinationAction=" + destinationAction + '}';
  }

  @Override
  public void updateRequestContent(int telegramId) {
    id = telegramId;
  }
}
