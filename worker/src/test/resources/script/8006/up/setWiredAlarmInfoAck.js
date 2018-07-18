var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setWiredAlarmInfoAck
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
    if (input.data.result != 0) {
        logger.error("control part failed: unkown result");
    }
    logger.info("control part succeeded");
    var list = new List();
    var part = new Map();
    part.protectNo = input.data.identifyNum == 0 ? 98 : 99;
    part.partType = "LH0FC";
    part.sirenDuration = input.data.alarmTime;
    part.runningState = input.data.alarmStatus;
    part.sirenSwitch = input.data.enable  == 1;

    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = part;
    list.add(partReport);

    if (input.data.identifyNum == 0) {
        //同步更新设备的警笛时长
        var deviceReport = new Map();
        deviceReport.type = "report";
        deviceReport.command = "deviceReport";
        deviceReport.data = new Map();
        deviceReport.data.sirenDuration = input.data.alarmTime;
        list.add(deviceReport);
    }

    return list;
}