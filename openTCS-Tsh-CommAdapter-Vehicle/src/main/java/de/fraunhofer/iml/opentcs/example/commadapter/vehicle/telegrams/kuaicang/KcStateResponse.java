package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang;

import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;

public class KcStateResponse extends StateResponse {
    private boolean success;
    private String errorMsg;
    private KcStateData data;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public KcStateData getData() {
        return data;
    }

    public void setData(KcStateData data) {
        this.data = data;
    }




    @Override
    public int getPositionId() {
        return super.getPositionId();
    }

    @Override
    public OperatingState getOperatingState() {
        return super.getOperatingState();
    }

    @Override
    public LoadState getLoadState() {
        return super.getLoadState();
    }

    @Override
    public int getLastReceivedOrderId() {
        return super.getLastReceivedOrderId();
    }

    @Override
    public int getCurrentOrderId() {
        return super.getCurrentOrderId();
    }

    @Override
    public int getLastFinishedOrderId() {
        return super.getLastFinishedOrderId();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void setPositionId(int positionId) {
        super.setPositionId(positionId);
    }

    @Override
    public void setOperatingState(OperatingState operatingState) {
        super.setOperatingState(operatingState);
    }

    @Override
    public void setLoadState(LoadState loadState) {
        super.setLoadState(loadState);
    }

    @Override
    public void setLastReceivedOrderId(int lastReceivedOrderId) {
        super.setLastReceivedOrderId(lastReceivedOrderId);
    }

    @Override
    public void setCurrentOrderId(int currentOrderId) {
        super.setCurrentOrderId(currentOrderId);
    }

    @Override
    public void setLastFinishedOrderId(int lastFinishedOrderId) {
        super.setLastFinishedOrderId(lastFinishedOrderId);
    }

    @Override
    public int getEnergyLevel() {
        return super.getEnergyLevel();
    }

    @Override
    public void setEnergyLevel(int energyLevel) {
        super.setEnergyLevel(energyLevel);
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }
}
