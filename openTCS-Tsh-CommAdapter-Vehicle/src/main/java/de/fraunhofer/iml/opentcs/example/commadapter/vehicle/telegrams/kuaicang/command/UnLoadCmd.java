package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command;


public class UnLoadCmd extends KcCmd {
    private CMD cmd;
    private int actionCode;


    @Override
    public CMD getCmd() {
        return cmd;
    }

    @Override
    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public UnLoadCmd() {
        cmd=CMD.DoRollerActionOperation;
        actionCode=2;
    }



    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }
}
