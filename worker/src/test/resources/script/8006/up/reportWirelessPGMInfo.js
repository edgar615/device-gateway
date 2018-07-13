var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessPGMInfo
function execute(input, logger) {

    var list = new List();
    //上报无线探测器校验和
    var deviceReport = new Map();
    deviceReport.type = "report";
    deviceReport.command = "deviceReport";
    deviceReport.data = new Map();
    deviceReport.data.wireLessPgmCheckNum = input.data.checknum;
    list.add(deviceReport);
    //107 有线PGM 108~112 无线PGM
    var isRegst = input.data.isRegst;
    var deletedParts = new List();
    for (var i = 0; i < 5; i ++) {
        if (isRegst.charAt(i) == 0) {
            deletedParts.add(i + 108);
        }
    }
    var deletedPartSync = new Map();
    deletedPartSync.type = "report";
    deletedPartSync.command = "deletedPartSync";
    deletedPartSync.data = new Map();
    deletedPartSync.data.parts = deletedParts;
    list.add(deletedPartSync);

    for (var i = 0; i < input.partInfo.length; i ++) {
        var partInfo = input.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.defenceNum + 108;
        part.barcode = partInfo.pgmID;
        part.workMode = partInfo.workMode;
        part.pgmTimer1 = part.time1Enable == 1;
        part.openHour1 = partInfo.openHour1;
        part.openMinute1 = partInfo.openMinute1;
        part.closeHour1 = partInfo.closeHour1;
        part.closeMinute1 = partInfo.closeMinute1;
        part.week1 = partInfo.week1;
        part.pgmTimer2 = part.time2Enable == 1;
        part.openHour2 = partInfo.openHour2;
        part.openMinute2 = partInfo.openMinute2;
        part.closeHour2 = partInfo.closeHour2;
        part.closeMinute2 = partInfo.closeMinute2;
        part.week2 = partInfo.week2;
        part.autoPartId = partInfo.triggerSrc;
        part.workTime = partInfo.workTime;

        //调制方式：FSK ASK ZGB，忽略
        part.modulationMode = partInfo.modulationMode;
        part.version = partInfo.version;

        var partReport = new Map();
        partReport.type = "report";
        partReport.command = "partReport";
        partReport.data = part;
        partReport.data.partType = "pgm";
        list.add(partReport);

        if (partInfo.actionType == 1) {
            //var event = new Map();
            //event.type = "event";
            //event.command = "new";
            //event.data = new Map();
            ////时间转换
            ////event.data.time = time + timezone * 60 * 60;
            //event.data.level = 1;
            //event.data.push = false;
            //event.data.defend = false;
            //event.data.protectNo = partInfo.defenceNum;
            //list.add(event);
        }
    }
    return list;
}