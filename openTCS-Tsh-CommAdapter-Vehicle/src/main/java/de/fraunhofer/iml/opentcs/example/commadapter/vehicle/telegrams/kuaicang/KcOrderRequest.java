package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang;

import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command.KcCmd;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderAction;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderRequest;

import java.util.List;

public class KcOrderRequest extends OrderRequest {

    private final List<KcCmd> cmds;


    /**
     * Creates a new instance.
     *
     * @param telegramId        The request's telegram id.
     * @param taskId            The order id.
     * @param destinationId     The name of the destination point.
     * @param destinationAction The action to execute at the destination point.
     */
    public KcOrderRequest(int telegramId, int taskId, int destinationId, OrderAction destinationAction,List<KcCmd> cmds) {
        super(telegramId, taskId, destinationId, destinationAction);
        this.cmds=cmds;
    }

    public List<KcCmd> getCmds() {
        return cmds;
    }
}
