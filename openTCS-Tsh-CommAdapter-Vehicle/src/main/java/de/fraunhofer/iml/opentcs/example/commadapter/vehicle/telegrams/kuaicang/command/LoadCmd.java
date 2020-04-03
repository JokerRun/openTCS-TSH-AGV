package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command;


public class LoadCmd extends KcCmd {
    private CMD cmd;
    private int actionCode;


    public LoadCmd() {
        cmd=CMD.DoRollerActionOperation;
        actionCode=1;
    }

    @Override
    public CMD getCmd() {
        return cmd;
    }

    @Override
    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }
}
