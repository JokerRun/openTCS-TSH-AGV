package telegrams;

import common.telegrams.LoadAction;

/**
 * Defines all actions that a vehicle can execute as part of an order.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public enum OrderAction {
  /**
   * No action.
   */
  NONE,
  /**
   * Action to load an object.
   */
  LOAD,
  /**
   * Action to unload an object.
   */
  UNLOAD,
  /**
   * Charge vehicle.
   */
  CHARGE;


  /**
   * Maps the given <code>actionString</code> to an Action.
   *
   * @param actionString
   * @return The Action associated with the <code>actionString</code>. Returns
   * <code>Action.NONE</code> if there isn't any Action associated with the <code>actionString</code>.
   */
  public static OrderAction stringToAction(String actionString) {
    OrderAction action = NONE;
    if (actionString.equals(LoadAction.LOAD)) {
      action = LOAD;
    }
    if (actionString.equals(LoadAction.UNLOAD)) {
      action = UNLOAD;
    }
    if (actionString.equals(LoadAction.CHARGE)) {
      action = CHARGE;
    }
    return action;
  }
}
