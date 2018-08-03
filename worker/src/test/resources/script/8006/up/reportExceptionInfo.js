var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up reportExceptionInfo
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.data.eventList.length; i++) {
        var alarm = input.data.eventList[i];
        //1无线探测器故障，2无线遥控器故障，3无线警号故障，4主机故障，5无线键盘故障
        var operateType = alarm.device;
        //故障 1电池低压，2设备丢失，3交流故障，4通迅故障，5电话掉线，20无GSM模块，21无SIM卡，22 GSM无服务，23 GSM信号弱，24  PIN码锁定
        var errorType = alarm.eventType;
        //0 恢复 1故障
        var state = alarm.state;
        //0表示主机故障
        var deviceNum = alarm.deviceNum;
        var time = alarm.time;
        var timezone = new Integer(alarm.tz - 12);

        //事件
        var event = new Map();
        event.type = "event";
        event.command = "new";
        event.data = new Map();
        event.data.time = new Integer(time - timezone * 60 * 60);
        /** 0~63无线防区 64~65系统自带的两个有线防区 66~97扩展的32个有线防区
         *  98内置警号 99外接有线警号 100~106 无线警号
         *  107 有线PGM 108~112 无线PGM
         *  113~144 无线遥控器
         *  145~176 RFID
         **/
        event.data.type = 41000;
        if (state == 1) {
            event.data.push = true;
        }
        if (operateType == 1) {
            //无线
            event.data.protectNo = deviceNum;
            if (errorType == 1) {//低压
                if (state == 1) {//故障
                    event.data.type = 41031;
                } else {//恢复
                    event.data.type = 41030;
                }
            }
            if (errorType == 2) {//离线
                if (state == 1) {//故障
                    event.data.type = 41041;
                } else {//恢复
                    event.data.type = 41040;
                }
            }
        } else if (operateType == 2) {
            //遥控器
            event.data.protectNo = new Integer(deviceNum + 113);
            if (errorType == 1) {//低压
                if (state == 1) {//故障
                    event.data.type = 41031;
                } else {//恢复
                    event.data.type = 41030;
                }
            }
        } else if (operateType == 3) {
            //警号
            event.data.protectNo = new Integer(deviceNum + 98);
        }else if (operateType == 4) {
            //主机
            if (errorType == 1) {
                if (state == 1) {
                    event.data.type = 43101;
                } else {
                    event.data.type = 43102;
                }
            }
            if (errorType == 2) {
                if (state == 1) {
                    event.data.type = 43103;
                } else {
                    event.data.type = 43104;
                }
            }
            if (errorType == 3) {
                if (state == 1) {
                    event.data.type = 43105;
                } else {
                    event.data.type = 43106;
                }
            }
            if (errorType == 4) {
                if (state == 1) {
                    event.data.type = 43107;
                } else {
                    event.data.type = 43108;
                }
            }
            if (errorType == 5) {
                if (state == 1) {
                    event.data.type = 43109;
                } else {
                    event.data.type = 43110;
                }
            }
            if (errorType == 6) {
                if (state == 1) {
                    event.data.type = 43111;
                } else {
                    event.data.type = 43112;
                }
            }
            if (errorType == 7) {
                if (state == 1) {
                    event.data.type = 43113;
                } else {
                    event.data.type = 43114;
                }
            }
            if (errorType == 8) {
                if (state == 1) {
                    event.data.type = 43115;
                } else {
                    event.data.type = 43116;
                }
            }
            if (errorType == 9) {
                if (state == 1) {
                    event.data.type = 43117;
                } else {
                    event.data.type = 43118;
                }
            }
        }else if (operateType == 5) {
            //键盘
            if (errorType == 1) {
                if (state == 1) {//故障
                    event.data.type = 43013;
                } else {//恢复
                    event.data.type = 43014;
                }
            }
            if (errorType == 2) {//丢失
                if (state == 1) {//故障
                    event.data.type = 43015;
                } else {//恢复
                    event.data.type = 43016;
                }
            }
        }
        list.add(event);
    }
    return list;
}