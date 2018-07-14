var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportSensorState
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.data.eventlist.length; i++) {
        var alarm = input.data.eventlist[i];
        //1 无线探测器 2 有线防区
        var device = alarm.device;
        //0 恢复 1触发
        var state = alarm.state;
        var areaNum = alarm.areaNum;
        var time = alarm.time;
        var timezone = alarm.tz - 12;

        //事件
        var event = new Map();
        event.type = "event";
        event.command = "new";
        event.data = new Map();
        event.data.protectNo = areaNum;
        event.data.time = time + timezone * 60 * 60;
        if (state == 0) {
            if (status == 1) {
                event.data.type = 44100;
            } else {
                event.data.type = 44101;
            }
        } else if (state == 1) {
            if (status == 1) {
                event.data.type = 44102;
            } else {
                event.data.type = 44103;
            }
        } else {
            event.data.type = 42000;
            logger.error("undefined:" + state);
        }
        list.add(event);
    }
    return list;
}