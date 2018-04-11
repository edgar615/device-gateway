var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

/**
 * 判断是否执行这个脚本
 * @param input
 */
function shouldExecute(input) {
    return "alarmF1Event" == input.command
        && "up" == input.type;
}

/**
 * 执行脚本
 * @param input
 * @returns {*}
 */
function execute(input) {
    var defend = input.data.defend;
    var list = new List();
    if (defend == 1) {
        var event = new Map();
        event.type = "event";
        event.command = "newEvent";
        event.data = new Map();
        event.data.alarmType = input.data.alarm;
        event.data.alarmTime = input.data.time;
        event.data.type = 42201;
        list.add(event);
    } else {
        var event = new Map();
        event.type = "log";
        event.command = "warn";
        event.data = new Map();
        event.data.message = "undefined defend:" + defend;
        list.add(event);
    }


    return list;
}