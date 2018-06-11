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
    report.command = "deviceReport";
    report.data = new Map();

    event.data.level = 1;
    event.data.time = Instant.now().getEpochSecond();
    var defend = input.data.defend;
    var list = new List();
    if (defend == 1) {
        event.data.type = 43001;
        report.data.defend = 1;
        list.add(event);
        list.add(report);
    } else if (defend == 2){
        event.data.type = 43003;
        report.data.defend = 3;
        list.add(event);
        list.add(report);
    } else {
        logger.error("undefined defend:" + defend);
    }
    return list;
}