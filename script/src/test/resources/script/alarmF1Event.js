var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
/**
 * 执行脚本
 * @param input
 * @returns {*}
 */
function execute(input, logger) {
    var defend = input.data.defend;
    var list = new List();
    if (defend == 1) {
        var event = new Map();
        event.type = "event";
        event.command = "newEvent";
        event.data = new Map();
        event.data.level = input.data.alarm;
        event.data.time = input.data.time;
        event.data.type = 42201;
        list.add(event);
    } else {
        logger.error("undefined defend:" + defend);
    }


    return list;
}