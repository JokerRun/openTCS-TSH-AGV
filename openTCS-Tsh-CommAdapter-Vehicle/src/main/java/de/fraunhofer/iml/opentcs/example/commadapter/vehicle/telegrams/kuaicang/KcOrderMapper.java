/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang;

import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command.ChargeCmd;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command.KcCmd;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command.LoadCmd;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command.MoveCmd;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderAction;
import de.fraunhofer.iml.opentcs.example.common.dispatching.LoadAction;
import de.fraunhofer.iml.opentcs.example.common.telegrams.BoundedCounter;
import de.fraunhofer.iml.opentcs.example.common.telegrams.Telegram;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Vehicle;
import org.opentcs.drivers.vehicle.MovementCommand;

import java.util.ArrayList;
import java.util.List;

import static de.fraunhofer.iml.opentcs.example.common.telegrams.BoundedCounter.UINT16_MAX_VALUE;

/**
 * Maps {@link MovementCommand}s from openTCS to a telegram sent to the vehicle.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public class KcOrderMapper {

    /**
     * Counts the order id's sent to the vehicle.
     */
    private final BoundedCounter orderIdCounter = new BoundedCounter(1, UINT16_MAX_VALUE);

    /**
     * Creates a new instance.
     */
    public KcOrderMapper() {
    }

    /**
     * Maps the given command to an order request that can be sent to the vehicle.
     *
     * @param command The command to be mapped.
     * @return The order request to be sent.
     * @throws IllegalArgumentException If the movement command could not be mapped properly.
     */
    public KcOrderRequest mapToOrder(MovementCommand command)
            throws IllegalArgumentException {



        List<KcCmd> cmds = new ArrayList<>();
        long sourceX = command.getStep().getSourcePoint().getPosition().getX();
        long sourceY = command.getStep().getSourcePoint().getPosition().getY();
        long targetX = command.getStep().getDestinationPoint().getPosition().getX();
        long targetY = command.getStep().getDestinationPoint().getPosition().getY();
        if (command.getOperation().equals(LoadAction.LOAD)) {
            cmds.add(new MoveCmd(sourceX,sourceY,targetX, targetY));
            cmds.add(new LoadCmd());
        } else if (command.getOperation().equals(LoadAction.UNLOAD)) {
            cmds.add(new MoveCmd(sourceX,sourceY,targetX, targetY));
            cmds.add(new LoadCmd());
        }else
        if (command.getOperation().equals(LoadAction.CHARGE)) {
            cmds.add(new ChargeCmd(targetX, targetY));
        }else
//            if (command.getOperation().equals(LoadAction.NONE)||command.getOperation().equals(LoadAction.PARK))
            {
            cmds.add(new MoveCmd(sourceX,sourceY,targetX, targetY));

        }

        return new KcOrderRequest(Telegram.ID_DEFAULT,
                orderIdCounter.getAndIncrement(),
                extractDestinationId(command.getStep().getDestinationPoint()),
                OrderAction.stringToAction(command.getOperation()), cmds);
    }


    private static int extractDestinationId(Point point)
            throws IllegalArgumentException {
        try {
            return Integer.parseInt(point.getName());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse point name: " + point.getName(), e);
        }
    }
}
