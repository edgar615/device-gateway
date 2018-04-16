var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Instant = Java.type("java.time.Instant");

function execute(input, logger) {
    var event = new Map();
    event.type = "event";
    event.command = "newEvent";
    event.data = new Map();
    var report = new Map();
    report.type = "report";
    report.command = "device.reported";
    report.data = new Map();

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
        logger.error("undefined defend:" + defend);
    }
    var list = new List();
    list.add(event);
    list.add(report);
    return list;
}