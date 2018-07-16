var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setWiredAlarmInfoAck
function execute(input, logger) {

    //0成功 1失败 2正在编程     2重复添加 4未知类型 5满
    if (input.data.result == 0) {
        logger.info("control part succeeded");
    }
    if (input.data.result == 1) {
        logger.info("control part failed");
        return
    }
    if (input.data.result == 2) {
        logger.info("control part failed: coding");
        return
    }
    if (input.data.result == 3) {
        logger.info("control part failed: already exists");
        return
    }
    if (input.data.result == 4) {
        logger.info("control part failed: undefined type");
        return
    }
    if (input.data.result == 5) {
        logger.info("control part failed: full");
        return;
    }
    var list = new List();
    var part = new Map();
    part.protectNo = input.data.identifyNum == 0 ? 98 : 99;
    part.partType = "LH0FC";
    part.sirenDuration = input.data.alarmTime;
    part.runningState = input.data.alarmStatus;
    part.sirenSwitch = input.data.enable  == 1;

    var event = new Map();
    event.type = "report";
    event.command = "partReport";
    event.data = part;
    list.add(event);
    return list;
}