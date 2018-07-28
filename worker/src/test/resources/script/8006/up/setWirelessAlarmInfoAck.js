var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up setWirelessAlarmInfoAck
function execute(input, logger) {

    //0成功 1失败 2正在编程     2重复添加 4未知类型 5满
    if (input.data.result == 1) {
        logger.error("control part failed");
        return new List();
    }
    if (input.data.result == 2) {
        logger.error("control part failed: coding");
        return new List();
    }
    if (input.data.result == 3) {
        logger.error("control part failed: already exists");
        return new List();
    }
    if (input.data.result == 4) {
        logger.error("control part failed: undefined type");
        return new List();
    }
    if (input.data.result == 5) {
        logger.error("control part failed: synchronizing");
        return new List();
    }
    if (input.data.result == 6) {
        logger.error("control part failed: full");
        return new List();
    }
    if (input.data.result != 0) {
        logger.error("control part failed: unkown result");
        return new List();
    }
    if (input.data.barcode == "0") {
        logger.info("delete part succeeded");
        var list = new List();
        var part = new Map();
        part.protectNo = new Integer(input.data.identifyNum + 100);
        var event = new Map();
        event.type = "report";
        event.command = "partDeleted";
        event.data = part;
        list.add(event);
        return list;
    }

    logger.info("control part succeeded");
    var list = new List();
    var part = new Map();
    part.protectNo = new Integer(input.data.identifyNum + 100);
    part.barcode = input.data.barcode;
    part.partType = input.data.barcode.substr(0, 5);
    part.partitionNo = input.data.partNum;
    part.sirenDuration = input.data.alarmTime;
    part.runningState = input.data.alarmStatus;
    if (input.data.alarmLamp == true || input.data.alarmLamp == 1) {
        part.lightSwitch = true;
    } else {
        part.lightSwitch = false;
    }
    if (input.data.enable == true || input.data.enable == 1) {
        part.sirenSwitch = true;
    } else {
        part.sirenSwitch = false;
    }
    part.sirenVolume = input.data.alarmVolume
    //调制方式：FSK ASK ZGB，忽略
    part.modulationMode = input.data.modulationMode;
    part.version = input.data.version;

    var event = new Map();
    event.type = "report";
    event.command = "partReport";
    event.data = part;
    list.add(event);
    return list;


}