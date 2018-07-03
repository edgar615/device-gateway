var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportAlarmEvent
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.eventlist.length; i ++) {
        var alarm = input.eventlist[i];
        //设备类型 1无线 2有线 3主机 4键盘
        var deviceType = alarm.device;
        //报警类型 1防拆 2阀值
        var alarmType = alarm.alarmType;
        //防区编号
        var areaNum = alarm.areaNum;
        var time = alarm.time;
        var timezone = alarm.tz - 12;
        var event = new Map();
        event.type = "event";
        event.command = "new";
        event.data = new Map();
        //时间转换
        event.data.time  = time + timezone * 60 * 60;
        if (alarmType == 1) {
            event.data.type = 41021;//拆动
        } else {
            event.data.type = 41011;//阈值
        }
        event.data.level = 200;
        event.data.push = true;
        event.data.defend = false;
        event.data.protectNo = areaNum;
        list.add(event);
    }
    return list;
}