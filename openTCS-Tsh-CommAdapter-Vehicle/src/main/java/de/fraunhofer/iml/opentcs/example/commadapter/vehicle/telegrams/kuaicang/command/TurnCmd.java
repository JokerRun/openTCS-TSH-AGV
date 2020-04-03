package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command;


public class TurnCmd extends KcCmd {
    private CMD cmd;
    private int omega;
    private int epsi;
    private int targetAngular;


    public TurnCmd() {
        this.cmd=CMD.DoTurnV2;
    }

    public TurnCmd(int targetAngular) {
        this.cmd=CMD.DoTurnV2;
        this.omega = targetAngular;
    }


    @Override
    public CMD getCmd() {
        return cmd;
    }

    @Override
    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public int getOmega() {
        return omega;
    }

    public void setOmega(int omega) {
        this.omega = omega;
    }

    public int getEpsi() {
        return epsi;
    }

    public void setEpsi(int epsi) {
        this.epsi = epsi;
    }

    public int getTargetAngular() {
        return targetAngular;
    }

    public void setTargetAngular(int targetAngular) {
        this.targetAngular = targetAngular;
    }
}
