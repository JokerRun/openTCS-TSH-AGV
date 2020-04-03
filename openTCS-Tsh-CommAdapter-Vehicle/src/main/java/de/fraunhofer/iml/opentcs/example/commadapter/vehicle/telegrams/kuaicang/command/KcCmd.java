package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.kuaicang.command;

public abstract class KcCmd {

    protected CMD cmd;


    public KcCmd() {
    }

    public KcCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public CMD getCmd() {
        return cmd;
    }

    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public enum CMD{
            /**
             * 装卸操作
             */
            DoRollerActionOperation,
            /**
             * 前移
             */
            DoMoveWithCodePosV2,



            /**
             * 后移(暂不做后退操作)
             */
    //        DoMoveBackWithCodePosV2,


            /**
             * 转弯
             */
            DoTurnV2,
            /**
             * 充电
             */
            DoChargeMoveV2,
            /**
             * 退出充电
             */
            DoExitChargingZoneV2;

        }
}
