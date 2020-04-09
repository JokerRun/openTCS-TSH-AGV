package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang;

import com.google.inject.Inject;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;
import org.opentcs.components.kernel.services.TCSObjectService;
import org.opentcs.data.model.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse.OperatingState.IDLE;

public class KcStateResponseMapper {
    private static final Logger LOG = LoggerFactory.getLogger(KcStateResponseMapper.class);

    private final TCSObjectService objectService;

    @Inject
    public KcStateResponseMapper(TCSObjectService objectService) {
        this.objectService = objectService;
    }


    public static void main(String[] args) {
        double d = 1.153;
        double e=(double)Math.round(d*10);
        System.out.println(e);
    }
    public void mapToStateResponse(KcStateResponse response, int previousPointId) {
        LOG.debug("报文转换KcStateResponse -> StateResponse");
        boolean success = response.isSuccess();
        if (response.getData() != null && success) {
            KcStateData data = response.getData();
            //根据小车上报点位，四舍五入小数点一位后的数值，再乘1000

            int x = (int) Math.round(data.getX()*10)*100;
            int y = (int) Math.round(data.getY()*10)*100;;
            int orderId = data.getTask_id();
            double heading = data.getHeading();
            String opState = data.getOpState();
            int power = data.getPower();
            boolean isLoad = data.isLoadState();


            int currentPointId = previousPointId;
            // 获取当前电位
            currentPointId = getCurrentPointId(x, y, currentPointId);
            response.setPositionId(currentPointId);
            response.setEnergyLevel(power);
            response.setLastFinishedOrderId(orderId);
            response.setLoadState(isLoad ? StateResponse.LoadState.FULL : StateResponse.LoadState.EMPTY);
            //TODO. 暂时一律为空闲状态
//            response.setOperatingState(stringToOperatingState(data.getOpState()));
            response.setOperatingState(IDLE);


        }

    }

    /**
     * 根据agv上报坐标，查找地图模型中的点位
     * @param x
     * @param y
     * @param currentOrderId
     * @return
     */
    private int getCurrentPointId(int x, int y, int currentOrderId) {
        Optional<Point> optionalPoint = objectService.fetchObjects(Point.class).stream()
                .filter(p -> p.getPosition().getX() == x && p.getPosition().getY() == y)
                .findFirst();
        if (optionalPoint.isPresent()) {
            currentOrderId=Integer.parseInt(optionalPoint.get().getName());
        }
        return currentOrderId;
    }
}
