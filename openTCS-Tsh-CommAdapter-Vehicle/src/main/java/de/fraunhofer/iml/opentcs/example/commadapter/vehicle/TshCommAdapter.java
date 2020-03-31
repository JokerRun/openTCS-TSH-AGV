/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle;

import com.alibaba.fastjson.JSONObject;
import com.google.inject.assistedinject.Assisted;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.comm.HttpUtil;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.TshProcessModelTO;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderRequest;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderResponse;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateRequest;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse.LoadState;
import de.fraunhofer.iml.opentcs.example.common.dispatching.LoadAction;
import de.fraunhofer.iml.opentcs.example.common.telegrams.*;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.drivers.vehicle.BasicVehicleCommAdapter;
import org.opentcs.drivers.vehicle.MovementCommand;
import org.opentcs.drivers.vehicle.VehicleProcessModel;
import org.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.opentcs.util.ExplainedBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static de.fraunhofer.iml.opentcs.example.common.telegrams.BoundedCounter.UINT16_MAX_VALUE;
import static java.util.Objects.requireNonNull;

/**
 * An example implementation for a communication adapter.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public class TshCommAdapter
        extends BasicVehicleCommAdapter
        implements
        TelegramSender {

    /**
     * This class's logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TshCommAdapter.class);
    /**
     * Maps movement commands from openTCS to the telegrams sent to the attached vehicle.
     */
    private final OrderMapper orderMapper;
    /**
     * The components factory.
     */
    private final TshAdapterComponentsFactory componentsFactory;
    /**
     * Manages counting the ids for all {@link Request} telegrams.
     */
    private final BoundedCounter globalRequestCounter = new BoundedCounter(0, UINT16_MAX_VALUE);
    /**
     * Maps commands to order IDs so we know which command to report as finished.
     */
    private final Map<MovementCommand, Integer> orderIds = new ConcurrentHashMap<>();
    /**
     * Matches requests to responses and holds a queue for pending requests.
     */
    private RequestResponseMatcher requestResponseMatcher;
    /**
     * A task for enqueuing state requests periodically.
     */
    private StateRequesterTask stateRequesterTask;

    /**
     * Creates a new instance.
     *
     * @param vehicle           The attached vehicle.
     * @param orderMapper       The order mapper for movement commands.
     * @param componentsFactory The components factory.
     */
    @Inject
    public TshCommAdapter(@Assisted Vehicle vehicle,
                          OrderMapper orderMapper,
                          TshAdapterComponentsFactory componentsFactory) {
        super(new TshProcessModel(vehicle), 1, 3, LoadAction.CHARGE);
        this.orderMapper = requireNonNull(orderMapper, "orderMapper");
        this.componentsFactory = requireNonNull(componentsFactory, "componentsFactory");
    }

    /**
     * 系统加载model时，会调用所有小车驱动的initialize方法。
     *
     * @author Rico
     * @date 2020/3/31 11:29 上午
     */
    @Override
    public void initialize() {
        LOG.info("初始化驱动{} ", getName());
        super.initialize();
        this.requestResponseMatcher = componentsFactory.createRequestResponseMatcher(this);
        ActionListener actionListener = e -> {
            LOG.debug("小车状态周期请求任务(stateRequesterTask) 即将enqueueRequest到消息队列...");
            requestResponseMatcher.enqueueRequest(new StateRequest());
        };
        this.stateRequesterTask = componentsFactory.createStateRequesterTask(actionListener);
    }

    @Override
    public void terminate() {
        LOG.info("终止用驱动{}", getName());

        stateRequesterTask.disable();
        super.terminate();
    }

    @Override
    public synchronized void enable() {
        LOG.info("启用驱动{}", getName());
        if (isEnabled()) {
            return;
        }

        //Create the channel manager responsible for connections with the vehicle
        //Initialize the channel manager
        //      HttpClient

        super.enable();
    }

    @Override
    public synchronized void disable() {
        LOG.info("停用驱动{}", getName());

        if (!isEnabled()) {
            return;
        }

        super.disable();
    }

    @Override
    public synchronized void clearCommandQueue() {
        LOG.info("{}:clearCommandQueue清空当前小车驱动的所有待执行指令。");
        super.clearCommandQueue();
        orderIds.clear();
    }

    @Override
    protected synchronized void connectVehicle() {

        String stateResponseJson = HttpUtil.doGet("http://" + getProcessModel().getVehicleHost() + ":" + getProcessModel().getVehiclePort() + "/api/getAgvInfo");

        if (!Objects.isNull(stateResponseJson)) {
            LOG.info("{}: 连接成功", getName());
            getProcessModel().setCommAdapterConnected(true);
            // Check for resending last request
            requestResponseMatcher.checkForSendingNextRequest();
        }


    }

    @Override
    protected synchronized void disconnectVehicle() {
        getProcessModel().setCommAdapterConnected(false);
        getProcessModel().setVehicleIdle(true);
        getProcessModel().setVehicleState(Vehicle.State.UNKNOWN);
        LOG.info("{}: 驱动已断开连接", getName());
    }

    @Override
    protected synchronized boolean isVehicleConnected() {
        return getProcessModel().isCommAdapterConnected();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (!(evt.getSource() instanceof TshProcessModel)) {
            return;
        }

        // Handling of events from the vehicle gui panels start here
        if (Objects.equals(evt.getPropertyName(),
                VehicleProcessModel.Attribute.COMM_ADAPTER_CONNECTED.name())
                || Objects.equals(evt.getPropertyName(),
                TshProcessModel.Attribute.PERIODIC_STATE_REQUESTS_ENABLED.name())) {
            if (getProcessModel().isCommAdapterConnected()
                    && getProcessModel().isPeriodicStateRequestEnabled()) {
                stateRequesterTask.setRequestInterval(getProcessModel().getStateRequestInterval());
                stateRequesterTask.enable();
            } else {
                stateRequesterTask.disable();
            }
        }
        if (Objects.equals(evt.getPropertyName(),
                TshProcessModel.Attribute.PERIOD_STATE_REQUESTS_INTERVAL.name())) {
            stateRequesterTask.setRequestInterval(getProcessModel().getStateRequestInterval());
        }
    }

    @Override
    @Deprecated
    protected List<org.opentcs.drivers.vehicle.VehicleCommAdapterPanel> createAdapterPanels() {
        return new ArrayList<>();
    }

    @Override
    public final TshProcessModel getProcessModel() {
        return (TshProcessModel) super.getProcessModel();
    }

    @Override
    protected VehicleProcessModelTO createCustomTransferableProcessModel() {
        //Add extra information of the vehicle when sending to other software like control center or
        //plant overview
        return new TshProcessModelTO()
                .setVehicleRef(getProcessModel().getVehicleReference())
                .setCurrentState(getProcessModel().getCurrentState())
                .setPreviousState(getProcessModel().getPreviousState())
                .setLastOrderSent(getProcessModel().getLastOrderSent())
                .setDisconnectingOnVehicleIdle(getProcessModel().isDisconnectingOnVehicleIdle())
                .setLoggingEnabled(getProcessModel().isLoggingEnabled())
                .setReconnectDelay(getProcessModel().getReconnectDelay())
                .setReconnectingOnConnectionLoss(getProcessModel().isReconnectingOnConnectionLoss())
                .setVehicleHost(getProcessModel().getVehicleHost())
                .setVehicleIdle(getProcessModel().isVehicleIdle())
                .setVehicleIdleTimeout(getProcessModel().getVehicleIdleTimeout())
                .setVehiclePort(getProcessModel().getVehiclePort())
                .setPeriodicStateRequestEnabled(getProcessModel().isPeriodicStateRequestEnabled())
                .setStateRequestInterval(getProcessModel().getStateRequestInterval());
    }

    @Override
    public synchronized void sendCommand(MovementCommand cmd)
            throws IllegalArgumentException {
        requireNonNull(cmd, "cmd");
        LOG.debug("{}:sendCommand收到系统下发的新命令: {}, 即将加入到orderIds,并enqueueRequest");
        try {
            OrderRequest telegram = orderMapper.mapToOrder(cmd);
            orderIds.put(cmd, telegram.getOrderId());
            LOG.debug("{}: Enqueuing order telegram with ID {}: {}, {}", getName(), telegram.getOrderId(), telegram.getDestinationId(), telegram.getDestinationAction());

            // Add the telegram to the queue. Telegram will be send later when its the first telegram in
            // the queue. This ensures that we always wait for a response until we send a new request.
            requestResponseMatcher.enqueueRequest(telegram);
            LOG.debug("{}: Finished enqueuing order telegram.", getName());
        } catch (IllegalArgumentException exc) {
            LOG.error("{}: Failed to enqueue command {}", getName(), cmd, exc);
        }
    }

    @Override
    public synchronized ExplainedBoolean canProcess(List<String> operations) {
        requireNonNull(operations, "operations");
        boolean canProcess = true;
        String reason = "";
        if (!isEnabled()) {
            canProcess = false;
            reason = "Adapter not enabled";
        }
        if (canProcess && !isVehicleConnected()) {
            canProcess = false;
            reason = "Vehicle does not seem to be connected";
        }
        if (canProcess
                && getProcessModel().getCurrentState().getLoadState() == LoadState.UNKNOWN) {
            canProcess = false;
            reason = "Vehicle's load state is undefined";
        }
        boolean loaded = getProcessModel().getCurrentState().getLoadState() == LoadState.FULL;
        final Iterator<String> opIter = operations.iterator();
        while (canProcess && opIter.hasNext()) {
            final String nextOp = opIter.next();
            // If we're loaded, we cannot load another piece, but could unload.
            if (loaded) {
                if (nextOp.startsWith(LoadAction.LOAD)) {
                    canProcess = false;
                    reason = "Cannot load when already loaded";
                } else if (nextOp.startsWith(LoadAction.UNLOAD)) {
                    loaded = false;
                } else if (nextOp.startsWith(DriveOrder.Destination.OP_PARK)) {
                    canProcess = false;
                    reason = "Vehicle shouldn't park while in a loaded state.";
                } else if (nextOp.startsWith(LoadAction.CHARGE)) {
                    canProcess = false;
                    reason = "Vehicle shouldn't charge while in a loaded state.";
                }
            }
            // If we're not loaded, we could load, but not unload.
            else if (nextOp.startsWith(LoadAction.LOAD)) {
                loaded = true;
            } else if (nextOp.startsWith(LoadAction.UNLOAD)) {
                canProcess = false;
                reason = "Cannot unload when not loaded";
            }
        }
        return new ExplainedBoolean(canProcess, reason);
    }

    @Override
    public void processMessage(Object message) {
        //Process messages sent from the kernel or a kernel extension
    }


    @Override
    public synchronized void onIncomingTelegram(Response response) {
        requireNonNull(response, "response");

        // Remember that we have received a sign of life from the vehicle
        getProcessModel().setVehicleIdle(false);

        //Check if the response matches the current request
        if (!requestResponseMatcher.tryMatchWithCurrentRequest(response)) {
            // XXX Either ignore the message or close the connection
            return;
        }

        if (response instanceof StateResponse) {
            onStateResponse((StateResponse) response);
        } else if (response instanceof OrderResponse) {
            LOG.debug("{}: Received a new order response: {}", getName(), response);
        } else {
            LOG.warn("{}: Unhandled telegram implementation: {}",
                    getName(),
                    response.getClass().getName());
        }

        //Send the next telegram if one is waiting
        requestResponseMatcher.checkForSendingNextRequest();
    }

    @Override
    public synchronized Response sendTelegram(Request telegram) {
        requireNonNull(telegram, "telegram");
        if (!isVehicleConnected()) {
            LOG.debug("{}: 驱动未连接小车，无法发送请求报文： '{}'", getName(), telegram);
            return null;
        }

        // Update the request's id
        telegram.updateRequestContent(globalRequestCounter.getAndIncrement());

        LOG.debug("{}: 驱动即将发送请求报文: '{}'", getName(), telegram);
        //    vehicleChannelManager.send(telegram);

        // If the telegram is an order, remember it.
        if (telegram instanceof StateRequest) {
            String stateResponseJson = HttpUtil.doGet("http://" + getProcessModel().getVehicleHost() + ":" + getProcessModel().getVehiclePort() + "/api/getAgvInfo");
            StateResponse stateResponse = JSONObject.parseObject(stateResponseJson, StateResponse.class);

            return stateResponse;
        } else if (telegram instanceof OrderRequest) {
            String orderRequestJson = JSONObject.toJSONString(telegram);
            //            String orderResponseJson = HttpUtil.doGet("http://" + getProcessModel().getVehicleHost() + ":" + getProcessModel().getVehiclePort() + "/api/CommandOrder");
            String orderResponseJson = null;
            try {
                orderResponseJson = HttpUtil.doPost("http://" + getProcessModel().getVehicleHost() + ":" + getProcessModel().getVehiclePort() + "/api/commandOrder", orderRequestJson);
                // If the telegram is an order, remember it.
                getProcessModel().setLastOrderSent((OrderRequest) telegram);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OrderResponse orderResponse = JSONObject.parseObject(orderResponseJson, OrderResponse.class);


            return orderResponse;
        }

        if (getProcessModel().isPeriodicStateRequestEnabled()) {
            stateRequesterTask.restart();
        }
        return null;
    }

    public RequestResponseMatcher getRequestResponseMatcher() {
        return requestResponseMatcher;
    }

    private void onStateResponse(StateResponse stateResponse) {
        LOG.debug("============ {}: onStateResponse 收到新的小车状态response: {} ==================", getName(), stateResponse);

        // Update the vehicle's current state and remember the old one.
        getProcessModel().setPreviousState(getProcessModel().getCurrentState());
        getProcessModel().setCurrentState(stateResponse);

        StateResponse previousState = getProcessModel().getPreviousState();
        StateResponse currentState = getProcessModel().getCurrentState();

        checkForVehiclePositionUpdate(previousState, currentState);
        checkForVehicleStateUpdate(previousState, currentState);
        checkOrderFinished(previousState, currentState);
        LOG.debug("============ {}: onStateResponse 完成小车状态response处理 ==================");

        // XXX Process further state updates extracted from the telegram here.
    }

    private void checkForVehiclePositionUpdate(StateResponse previousState,
                                               StateResponse currentState) {
        if (previousState.getPositionId() == currentState.getPositionId()) {
            return;
        }
        // Map the reported position ID to a point name.
        String currentPosition = String.valueOf(currentState.getPositionId());
        LOG.debug("{}: onStateResponse.1 检查到小车位置发生变化，当前所在点位为：{}", getName(), currentPosition);
        // Update the position with the rest of the system, but only if it's not zero (unknown).
        if (currentState.getPositionId() != 0) {
            getProcessModel().setVehiclePosition(currentPosition);
        }
    }

    private void checkForVehicleStateUpdate(StateResponse previousState,
                                            StateResponse currentState)  {
        if (previousState.getOperatingState() == currentState.getOperatingState()) {
            return;
        }
        LOG.debug("{}: onStateResponse.2 检查到状态发生变化，更新内核小车状态数据 ");

        getProcessModel().setVehicleState(translateVehicleState(currentState.getOperatingState()));
    }

    private void checkOrderFinished(StateResponse previousState, StateResponse currentState) {
        if (currentState.getLastFinishedOrderId() == 0) {
            return;
        }
        // If the last finished order ID hasn't changed, don't bother.
        if (previousState.getLastFinishedOrderId() == currentState.getLastFinishedOrderId()) {
            return;
        }
        // Check if the new finished order ID is in the queue of sent orders.
        // If yes, report all orders up to that one as finished.
        if (!orderIds.containsValue(currentState.getLastFinishedOrderId())) {
            LOG.debug("{}: Ignored finished order ID {} (reported by vehicle, not found in sent queue).",
                    getName(),
                    currentState.getLastFinishedOrderId());
            return;
        }
        LOG.debug("{}: onStateResponse.3.1 检查到完成了一个新的指令单{} ",currentState.getLastFinishedOrderId());

        //遍历所有已经被下发的指令，
        Iterator<MovementCommand> sentCmd = getSentQueue().iterator();
        boolean finishedAll = false;
        while (!finishedAll && sentCmd.hasNext()) {
            MovementCommand cmd = sentCmd.next();
            sentCmd.remove();
            int orderId = orderIds.remove(cmd);
            if (orderId == currentState.getLastFinishedOrderId()) {
                finishedAll = true;
            }

            LOG.debug("{}: onStateResponse.3.2 告诉系统id为：{} 的指令单: {}已被完成", getName(), orderId, cmd);

            getProcessModel().commandExecuted(cmd);
        }
    }

    /**
     * Map the vehicle's operation states to the kernel's vehicle states.
     *
     * @param operationState The vehicle's current operation state.
     */
    private Vehicle.State translateVehicleState(StateResponse.OperatingState operationState) {
        switch (operationState) {
            case IDLE:
                return Vehicle.State.IDLE;
            case MOVING:
            case ACTING:
                return Vehicle.State.EXECUTING;
            case CHARGING:
                return Vehicle.State.CHARGING;
            case ERROR:
                return Vehicle.State.ERROR;
            default:
                return Vehicle.State.UNKNOWN;
        }
    }

}
