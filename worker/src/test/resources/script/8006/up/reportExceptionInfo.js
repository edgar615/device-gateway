var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportExceptionInfo
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.data.eventlist.length; i++) {
        var alarm = input.data.eventlist[i];
        //1无线探测器故障，2无线遥控器故障，3无线警号故障，4主机故障，5无线键盘故障
        var operateType = alarm.device;
        //故障 1电池低压，2设备丢失，3交流故障，4通迅故障，5电话掉线，20无GSM模块，21无SIM卡，22 GSM无服务，23 GSM信号弱，24  PIN码锁定
        var errorType = alarm.eventType;
        //0 恢复 1故障
        var status = alarm.status;
        //0表示主机故障
        var deviceNum = alarm.deviceNum;
        var time = alarm.time;
        var timezone = alarm.tz - 12;

        //事件
        var event = new Map();
        event.type = "event";
        event.command = "new";
        event.data = new Map();
        event.data.time = time + timezone * 60 * 60;
        /** 0~63无线防区 64~65系统自带的两个有线防区 66~97扩展的32个有线防区
         *  98内置警号 99外接有线警号 100~106 无线警号
         *  107 有线PGM 108~112 无线PGM
         *  113~144 无线遥控器
         *  145~176 RFID
         **/
        if (operateType == 1) {
            //键盘
            event.data.protectNo = deviceNum;
        } else if (operatorType == 2) {
            //遥控器
            event.data.protectNo = deviceNum + 113;
        } else if (operatorType == 3) {
            //警号
            event.data.protectNo = deviceNum + 98;
        }else if (operatorType == 5) {
            //短信
            event.data.protectNo = deviceNum + 107;
        }

        if (errorType == 1) {
            if (status == 1) {
                event.data.type = 44011;
            } else {
                event.data.type = 44012;
            }
        } else if (errorType == 2) {
            if (status == 1) {
                event.data.type = 44013;
            } else {
                event.data.type = 44014;
            }
        } else if (errorType == 3) {
            if (status == 1) {
                event.data.type = 44015;
            } else {
                event.data.type = 44016;
            }
        } else if (errorType == 4) {
            if (status == 4) {
                event.data.type = 44017;
            } else {
                event.data.type = 44018;
            }
        } else if (errorType == 5) {
            if (status == 1) {
                event.data.type = 44019;
            } else {
                event.data.type = 44020;
            }
        } else if (errorType == 20) {
            if (status == 1) {
                event.data.type = 44021;
            } else {
                event.data.type = 44022;
            }
        } else if (errorType == 21) {
            if (status == 1) {
                event.data.type = 44023;
            } else {
                event.data.type = 44024;
            }
        } else if (errorType == 22) {
            if (status == 1) {
                event.data.type = 44025;
            } else {
                event.data.type = 44026;
            }
        } else if (errorType == 23) {
            if (status == 1) {
                event.data.type = 44027;
            } else {
                event.data.type = 44028;
            }
        } else if (errorType == 24) {
            if (status == 1) {
                event.data.type = 44029;
            } else {
                event.data.type = 44030;
            }
        } else {
            event.data.type = 42000;
            logger.error("undefined:" + errorType);
        }
        list.add(event);
    }
    return list;
}