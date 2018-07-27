var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up reportWirelessPGMInfo
function execute(input, logger) {

    var list = new List();
    var partRegistrySync = new Map();
    partRegistrySync.type = "report";
    partRegistrySync.command = "partRegistrySync";
    partRegistrySync.data = new Map();
    partRegistrySync.data.startIndex = 108;
    partRegistrySync.data.endIndex = 112;
    partRegistrySync.data.registryType = "wirelessPgm";
    partRegistrySync.data.partRegistry = input.data.isRegst;
    list.add(partRegistrySync);

    if (input.data.partInfo.length == 0) {
        return list;
    }
    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = new Map();
    partReport.data.parts = new List();
    list.add(partReport);
    //107 有线PGM 108~112 无线PGM
    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.protectNo = new Integer(partInfo.identifyNum + 108);
        part.barcode = partInfo.pgmID;
        part.partType = partInfo.pgmID.substr(0, 5);
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
        part.triggerPart = partInfo.triggerSrc;
        part.workTime = partInfo.workTime;

        //调制方式：FSK ASK ZGB，忽略
        part.modulationMode = partInfo.modulationMode;
        part.version = partInfo.version;
        partReport.data.parts.add(part);
    }
    return list;
}