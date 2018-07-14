var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setWiredPGMAck
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
    part.partType = "LH0FE";
    part.protectNo = input.data.defenceNum + 108;
    part.barcode = input.data.pgmID;
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
    part.autoPartId = input.data.triggerSrc;
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