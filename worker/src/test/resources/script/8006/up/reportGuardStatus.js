var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportGuardStatus
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.data.eventlist.length; i ++) {
        var alarm = input.data.eventlist[i];
        //1表示APP上布撤防，2表示键盘上布撤防，3表示遥控器布撤防（编号0到31），4表示RFID布撤防，5表示短信布撤防，6表示电话布撤防
        var operatorType = alarm.userNum;
        //设备变化 主要描述在键盘、遥控器、RFID布撤防时对应键盘、遥控器、RFID的编号
        var deviceNum = alarm.deviceNum;
        //分区号
        var areaNum = alarm.areaNum;
        var time = alarm.time;
        var timezone = alarm.tz - 12;
        var event = new Map();
        event.type = "event";
        event.command = "new";
        event.data = new Map();
        //时间转换
        event.data.time  = time + timezone * 60 * 60;
        var status = alarm.status;
        if (status == 0) {
            //撤防
            event.data.type = 43003;
        } else if (status == 1) {
            event.data.type = 43001;//外出
        } else if (status == 2) {
            event.data.type = 43002;//在家
        }else {
            event.data.type = 43000;//未定义
        }
        event.data.level = 1;
        event.data.push = true;
        event.data.defend = true;
        event.data.partitionNo = areaNum;
        /** 0~63无线防区 64~65系统自带的两个有线防区 66~97扩展的32个有线防区
         *  98内置警号 99外接有线警号 100~106 无线警号
         *  107 有线PGM 108~112 无线PGM
         *  113~144 无线遥控器
         *  145~176 RFID
         **/
        if (operatorType == 2) {
            //键盘
            event.data.protectNo = areaNum + 107;
        } else if (operatorType == 3) {
            //遥控器
            event.data.protectNo = areaNum + 113;
        } else if (operatorType == 4) {
            //RFID
            event.data.protectNo = areaNum + 145;
        }else if (operatorType == 5) {
            //短信
            event.data.operator = input.data.call;
        }else if (operatorType == 6) {
            //电话
            event.data.operator = input.data.call;
        }
        list.add(event);
    }
    return list;
}