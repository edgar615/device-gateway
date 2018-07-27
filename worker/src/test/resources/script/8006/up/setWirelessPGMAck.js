var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up setWirelessPGMAck
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

    if (input.data.pgmID == "0") {
        logger.info("delete part succeeded");
        var list = new List();
        var part = new Map();
        part.protectNo = new Integer(input.data.identifyNum + 108);
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
    part.protectNo = new Integer(input.data.identifyNum + 108);
    part.barcode = input.data.pgmID;
    part.partType = "LH0FE";
    part.workMode = input.data.workMode;
    part.pgmTimer1 = part.time1Enable == 1;
    part.openHour1 = input.data.openHour1;
    part.openMinute1 = input.data.openMinute1;
    part.closeHour1 = input.data.closeHour1;
    part.closeMinute1 = input.data.closeMinute1;
    part.week1 = input.data.week1;
    part.pgmTimer2 = part.time2Enable == 1;
    part.openHour2 = input.data.openHour2;
    part.openMinute2 = input.data.openMinute2;
    part.closeHour2 = input.data.closeHour2;
    part.closeMinute2 = input.data.closeMinute2;
    part.week2 = input.data.week2;
    part.triggerPart = input.data.triggerSrc;
    part.workTime = input.data.workTime;

    //调制方式：FSK ASK ZGB，忽略
    part.modulationMode = input.data.modulationMode;
    part.version = input.data.version;

    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = part;
    list.add(partReport);
    return list;
}