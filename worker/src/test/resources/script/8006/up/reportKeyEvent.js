var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportKeyEvent
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.eventlist.length; i++) {
        var alarm = input.eventlist[i];
        //事件类型，1进入编程 2退出编程
        var eventType = alarm.eventType;
        //键盘编号，用来计算分区号
        var keyNum = alarm.keyNum;
        var passwdNum = alarm.passwdNum;
        var time = alarm.time;
        var timezone = alarm.tz - 12;

        var event = new Map();
        event.type = "event";
        event.command = "new";
        event.data = new Map();
        //时间转换
        event.data.time = time + timezone * 60 * 60;
        event.data.level = 1;
        event.data.push = false;
        event.data.defend = false;
        event.data.protectNo = keyNum + 107;
        //todo 定义类型
        if (eventType == 1) {
            event.data.type = 44001;
        } else if (eventType == 2){
            event.data.type = 44002;
        } else {
            event.data.type = 42000;
            logger.error("undefined: " + eventType);
        }
        list.add(event);
    }
    return list;
}