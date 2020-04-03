package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command;


public class ChargeCmd extends KcCmd {
    private CMD cmd;
    private int targetX;
    private int targetY;
    private int targetHeading;


    public ChargeCmd() {
        cmd=CMD.DoChargeMoveV2;
    }

    public ChargeCmd(int targetX, int targetY,int targetHeading) {
        cmd=CMD.DoChargeMoveV2;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetHeading = targetHeading;
    }

    public ChargeCmd(long targetX, long targetY) {
        cmd=CMD.DoChargeMoveV2;
        this.targetX = (int) targetX;
        this.targetY = (int) targetY;

    }

    @Override
    public CMD getCmd() {
        return cmd;
    }

    @Override
    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public int getTargetHeading() {
        return targetHeading;
    }

    public void setTargetHeading(int targetHeading) {
        this.targetHeading = targetHeading;
    }
}
