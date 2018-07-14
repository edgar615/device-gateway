var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessPGMInfo
function execute(input, logger) {

    var list = new List();

    var part = new Map();
    part.partType = "LH0FE";
    part.protectNo = 107;
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