package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang;

import com.google.inject.Inject;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;
import org.opentcs.components.kernel.services.TCSObjectService;
import org.opentcs.data.model.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse.OperatingState.stringToOperatingState;

public class KcStateResponseMapper {
    private static final Logger LOG = LoggerFactory.getLogger(KcStateResponseMapper.class);

    private final TCSObjectService objectService;

    @Inject
    public KcStateResponseMapper(TCSObjectService objectService) {
        this.objectService = objectService;
    }

    public void mapToStateResponse(KcStateResponse response, int previousPointId) {
        LOG.debug("报文转换KcStateResponse -> StateResponse");
        boolean success = response.isSuccess();
        if (response.getData() != null && success) {
            KcStateData data = response.getData();
            int x = data.getX();
            int y = data.getY();
            int orderId = data.getOrderId();
            double heading = data.getHeading();
            String opState = data.getOpState();
            int power = data.getPower();
            boolean isLoad = data.isLoadState();


            int currentOrderId = previousPointId;
            // 获取当前电位
            currentOrderId = getCurrentOrderId(x, y, currentOrderId);
            response.setCurrentOrderId(currentOrderId);
            response.setEnergyLevel(power);
            response.setLastFinishedOrderId(orderId);
            response.setLoadState(isLoad ? StateResponse.LoadState.FULL : StateResponse.LoadState.EMPTY);
            response.setOperatingState(stringToOperatingState(data.getOpState()));


        }

    }

    /**
     * 根据agv上报坐标，查找地图模型中的点位
     * @param x
     * @param y
     * @param currentOrderId
     * @return
     */
    private int getCurrentOrderId(int x, int y, int currentOrderId) {
        Optional<Point> optionalPoint = objectService.fetchObjects(Point.class).stream()
                .filter(p -> p.getPosition().getX() == x && p.getPosition().getY() == y)
                .findFirst();
        if (optionalPoint.isPresent()) {
            currentOrderId=Integer.parseInt(optionalPoint.get().getName());
        }
        return currentOrderId;
    }
}
