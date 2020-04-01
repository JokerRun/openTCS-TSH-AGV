/**
 * Copyright (c) Fraunhofer IML
 */

import com.alibaba.fastjson.JSONObject;
import common.telegrams.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegrams.OrderAction;
import telegrams.OrderRequest;
import telegrams.StateRequest;
import telegrams.VehicleState;

public class VehicleSimulator {

    public static void main(String[] args) {
        VehicleSimulator vehicleSimulator = new VehicleSimulator();
        StateRequest stateRequest = new StateRequest();
        String s = vehicleSimulator.onIncomingTelegram(stateRequest);
        System.out.println(s);

    }


    public String onStartRequest(){
        StateRequest stateRequest = new StateRequest();
        String stateResponse = onIncomingTelegram(stateRequest);
        return stateResponse;
    }

    public String onOrderRequest(String orderRequestJson){
        OrderRequest orderRequest = JSONObject.parseObject(orderRequestJson, OrderRequest.class);
        String orderResponse = onIncomingTelegram(orderRequest);
        return orderResponse;
    }

    public String setAgvInfo(String requestJson){
        VehicleState orderRequest = JSONObject.parseObject(requestJson, VehicleState.class);
        this.vehicleState.setEnergyLevel(orderRequest.getEnergyLevel());
        
        return this.vehicleState.toStateResponse();
    }
    






    /**
     * This class's logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(VehicleSimulator.class);

    /**
     * The internal state of the simulated vehicle.
     */
    private final VehicleState vehicleState = new VehicleState();

    public String onIncomingTelegram(Request request) {
        if (request instanceof StateRequest) {
            return createStateResponse();
        } else if (request instanceof OrderRequest) {
            LOG.info("Incoming OrderRequest : {}", request);
            LOG.info("小车收到TCS发来【订单报文】报文,id={},orderId={},destId={},destAction={}");

            vehicleState.setLastReceivedOrderId(((OrderRequest) request).getOrderId());
            simulateCompleteTask((OrderRequest) request);

            String response = createOrderResponse();
            LOG.info("Sending OrderRequest response: {}", response);
            return response;
        }
        return null;
    }

    /**
     * 模拟小车执行tcs下发的订单任务
     *
     * @author Rico
     * @date 2020/2/5 2:25 下午
     */
    private synchronized void simulateCompleteTask(OrderRequest request) {
        int requestId = request.getId();
        int orderID = request.getOrderId();
        int destId = request.getDestinationId();
        OrderAction destAction = request.getDestinationAction();
        if (vehicleState.getOperatingState() == VehicleState.OperatingState.IDLE) {
            vehicleState.setCurrOrderId(orderID);
            //执行移动: 更改小车运行状态为移动中 --> 更新小车当前点位
            if (vehicleState.getPositionId() != destId) {
                vehicleState.setOperatingState(VehicleState.OperatingState.MOVING);
                vehicleState.setEnergyLevel(vehicleState.getEnergyLevel()-1);
                vehicleState.setPositionId(destId);
            }
            //执行装卸动作:
            if (OrderAction.NONE == destAction) {
                vehicleState.setOperatingState(VehicleState.OperatingState.IDLE);
            } else if (OrderAction.LOAD == destAction) {
                vehicleState.setOperatingState(VehicleState.OperatingState.ACTING);
                vehicleState.setEnergyLevel(vehicleState.getEnergyLevel()-1);
                vehicleState.setLoadState(VehicleState.LoadState.FULL);
                vehicleState.setOperatingState(VehicleState.OperatingState.IDLE);
            } else if (OrderAction.UNLOAD == destAction) {
                vehicleState.setOperatingState(VehicleState.OperatingState.ACTING);
                vehicleState.setEnergyLevel(vehicleState.getEnergyLevel()-1);
                vehicleState.setLoadState(VehicleState.LoadState.FULL);
                vehicleState.setOperatingState(VehicleState.OperatingState.IDLE);
            }else if (OrderAction.CHARGE == destAction) {

            }
                vehicleState.setOperatingState(VehicleState.OperatingState.IDLE);
            vehicleState.setLastFinishedOrderId(orderID);
        }


    }


    /**
     * Creates a state response from the simulated vehicle state.
     *
     * @return The byte representation of a state response
     */
    private String createStateResponse() {
        return vehicleState.toStateResponse();
        //        return vehicleState.toStateResponse();
    }

    /**
     * Creates an order response from the simulated vehicle state.
     *
     * @return The byte representation of a state response
     */
    private String createOrderResponse() {
        return vehicleState.toOrderResponse();
        //        return vehicleState.toOrderResponse().getRawContent();
    }

}
