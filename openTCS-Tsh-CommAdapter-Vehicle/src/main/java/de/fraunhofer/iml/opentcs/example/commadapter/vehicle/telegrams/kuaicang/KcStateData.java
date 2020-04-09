package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang;

public class KcStateData {
    /**
     * 点位
     */
    private double x;
    private double y;

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
    private int task_id;

    /**
     * 执行状态
     */
    private String opState;


    public KcStateData() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getOpState() {
        return opState;
    }

    public void setOpState(String opState) {
        this.opState = opState;
    }
}
