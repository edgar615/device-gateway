var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirePGMInfo
function execute(input, logger) {

    var list = new List();

    var part = new Map();
    part.partType = "LH0FE";
    part.protectNo = 107;
    part.barcode = input.data.pgmID;
    part.workMode = input.data.workMode;
    part.pgmTimer1 = part.time1Enable == 1;
    part.openHour1 = input.data.openHour1;
    part.openMin1 = input.data.openMinute1;
    part.closeHour1 = input.data.closeHour1;
    part.closeMin1 = input.data.closeMinute1;
    part.week1 = input.data.week1;
    part.pgmTimer2 = part.time2Enable == 1;
    part.openHour2 = input.data.openHour2;
    part.openMin2 = input.data.openMinute2;
    part.closeHour2 = input.data.closeHour2;
    part.closeMin2 = input.data.closeMinute2;
    part.week2 = input.data.week2;
    part.workTime = input.data.workTime;
    if (input.data.triggerSrc >=0 && input.data.triggerSrc <= 97) {
        part.triggerPart = input.data.triggerSrc;
    }
    if (input.data.triggerSrc >=98 && input.data.triggerSrc <= 100) {
        part.triggerCondition = input.data.triggerSrc;
    }
    //调制方式：FSK ASK ZGB，忽略
    part.modulationMode = input.data.modulationMode;
    part.version = input.data.version;
    //名称
    if (input.data.name != undefined) {
        part.unicodeName = "";
        for (var j = 0; j < input.data.name.length; j ++) {
            part.unicodeName += "\\u" + input.data.name[j];
        }
    }
    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = part;
    list.add(partReport);
    return list;
}