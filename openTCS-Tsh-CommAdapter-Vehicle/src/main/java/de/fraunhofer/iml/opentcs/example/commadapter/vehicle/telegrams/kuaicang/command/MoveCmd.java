package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command;


public class MoveCmd extends KcCmd {
    private CMD cmd;
    private int source_x;
    private int source_y;
    private int target_x;
    private int target_y;

    public MoveCmd() {
        cmd=CMD.DoMoveWithCodePosV2;
    }


    public MoveCmd(int target_x, int target_y) {
        cmd=CMD.DoMoveWithCodePosV2;
        this.target_x = target_x;
        this.target_y = target_y;
    }



    public MoveCmd(long source_x, long source_y, long target_x, long target_y) {
        cmd=CMD.DoMoveWithCodePosV2;
        this.source_x = (int) source_x;
        this.source_y = (int) source_y;
        this.target_x = (int) target_x;
        this.target_y = (int) target_y;
    }

    @Override
    public CMD getCmd() {
        return cmd;
    }

    @Override
    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public int getSource_x() {
        return source_x;
    }

    public void setSource_x(int source_x) {
        this.source_x = source_x;
    }

    public int getSource_y() {
        return source_y;
    }

    public void setSource_y(int source_y) {
        this.source_y = source_y;
    }

    public int getTarget_x() {
        return target_x;
    }

    public void setTarget_x(int target_x) {
        this.target_x = target_x;
    }

    public int getTarget_y() {
        return target_y;
    }

    public void setTarget_y(int target_y) {
        this.target_y = target_y;
    }
}
