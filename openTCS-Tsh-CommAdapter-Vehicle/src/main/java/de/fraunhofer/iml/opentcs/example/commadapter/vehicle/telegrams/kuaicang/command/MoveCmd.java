package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command;


public class MoveCmd extends KcCmd {
    private CMD cmd;
    private int sourceX;
    private int sourceY;
    private int targetX;
    private int targetY;

    public MoveCmd() {
        cmd=CMD.DoMoveWithCodePosV2;
    }


    public MoveCmd(int targetX, int targetY) {
        cmd=CMD.DoMoveWithCodePosV2;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public MoveCmd(long targetX, long targetY) {
        cmd=CMD.DoMoveWithCodePosV2;
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

    public int getSourceX() {
        return sourceX;
    }

    public void setSourceX(int sourceX) {
        this.sourceX = sourceX;
    }

    public int getSourceY() {
        return sourceY;
    }

    public void setSourceY(int sourceY) {
        this.sourceY = sourceY;
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
}
