package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang;

public class KcStateData {
    /**
     * 点位
     */
    private int x;
    private int y;

    /**
     * 电量
     */
    private int power;

    /**
     * 集成度(手动/自动)
     */
    private boolean mode;

    /**
     * 装货情况
     */
    private boolean loadState;

    /**
     * 小车朝向
     */
    private double heading;

    /**
     * 已完成任务id
     */
    private int orderId;

    /**
     * 执行状态
     */
    private String opState;


    public KcStateData() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public boolean isLoadState() {
        return loadState;
    }

    public void setLoadState(boolean loadState) {
        this.loadState = loadState;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOpState() {
        return opState;
    }

    public void setOpState(String opState) {
        this.opState = opState;
    }
}
