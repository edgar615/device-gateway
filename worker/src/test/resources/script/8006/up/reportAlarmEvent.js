var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up reportAlarmEvent
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.data.eventlist.length; i++) {
        var alarm = input.data.eventlist[i];
        //设备类型 1无线 2有线 3主机 4键盘
        var deviceType = alarm.device;
        //报警类型 1防拆 2阀值
        var alarmType = alarm.alarmType;
        //防区编号
        var areaNum = alarm.areaNum;
        var time = alarm.time;
        var timezone = new Integer(alarm.tz - 12);
        var event = new Map();
        event.type = "event";
        event.command = "new";
        event.data = new Map();
        //时间转换
        event.data.time = new Integer(time - timezone * 60 * 60);
        if (alarmType == 1) {
            event.data.type = 41021;//拆动
        } else if (alarmType == 2)  {
            event.data.type = 41011;//阈值
        } else if (alarmType == 3)  {
            event.data.type = 42004;//SOS
        } else if (alarmType == 4)  {
            event.data.type = 42012;//挟持
        } else {
            logger.error("unkown alarmType");
        }
        event.data.level = 3;
        event.data.push = true;
        event.data.defend = false;
        /** 0~63无线防区 64~65系统自带的两个有线防区 66~97扩展的32个有线防区
         *  98内置警号 99外接有线警号 100~106 无线警号
         *  107 有线PGM 108~112 无线PGM
         *  113~144 无线遥控器
         *  145~176 RFID
         **/
        if (deviceType == 3) {
            //主机、没有防区号
            event.data.partitionNo = -1;
        } else if (deviceType == 1) {
            //无线 根据探测器的分区设置报警分区
            event.data.partitionAlarm = true;
            event.data.protectNo = areaNum;
        } else if (deviceType == 2) {
            //有线
            event.data.protectNo = areaNum;
        } else if (deviceType == 4) {
            //键盘
            event.data.protectNo = new Integer(areaNum + 107);
        } else if (deviceType == 5) {
            //遥控器
            event.data.protectNo = new Integer(areaNum + 113);
        }
        list.add(event);
        //有线防区同步激活配件
        if (deviceType == 2) {
            var partActive = new Map();
            partActive.type = "report";
            partActive.command = "partActive";
            partActive.data = new Map();
            partActive.data.partType = "LH0FD";
            partActive.data.protectNo = areaNum;
            list.add(partActive);
        }
    }
    return list;
}