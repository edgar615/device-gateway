var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Instant = Java.type("java.time.Instant");

/**
 * 判断是否执行这个脚本
 * @param input
 */
function shouldExecute(input) {
    return "setDefendF1Response" == input.command
        && "up" == input.type;
}

function execute(input) {
    var event = new Map();
    event.type = "event";
    event.command = "newEvent";
    event.data = new Map();
    var report = new Map();
    report.type = "report";
    report.command = "device.reported";
    report.data = new Map();

    var log = new Map();
    log.type = "log";
    log.command = "error";
    log.data = new Map();

    event.data.alarmType = 1;
    event.data.alarmTime = Instant.now().getEpochSecond();
    var defend = Instant.now().getEpochSecond();
    if (defend == 1) {
        event.data.type = 43001;
        report.data.defend = 1;
    } else if (defend == 2){
        event.data.type = 43003;
        report.data.defend = 3;
    } else {
        log.data.message = "unkown defend:" + defend;
    }
    var list = new List();
    list.add(event);
    list.add(report);
    list.add(log);
    return list;
}