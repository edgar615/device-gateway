var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessAlarmInfo
function execute(input, logger) {

    var list = new List();
    var checknum = input.data.checknum;
    //98内置警号 99外接有线警号 100~106 无线警号
    var partRegistrySync = new Map();
    partRegistrySync.type = "report";
    partRegistrySync.command = "partRegistrySync";
    partRegistrySync.data = new Map();
    partRegistrySync.data.startIndex = 100;
    partRegistrySync.data.endIndex = 106;
    partRegistrySync.data.registryType = "wirelessAlarm";
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

    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.identifyNum + 100;
        part.barcode = partInfo.barcode;
        part.partType = partInfo.barcode.substr(0, 5);
        part.partitionNo = partInfo.partNum;
        part.sirenDuration = partInfo.alarmTime;
        part.runningState = partInfo.alarmStatus;
        part.sirenVolume = partInfo.alarmVolume;
        if (partInfo.alarmLamp == true || partInfo.alarmLamp == 1) {
            part.lightSwitch = true;
        } else {
            part.lightSwitch = false;
        }
        if (partInfo.enable == true || partInfo.enable == 1) {
            part.sirenSwitch = true;
        } else {
            part.sirenSwitch = false;
        }

        //调制方式：FSK ASK ZGB，忽略
        part.modulationMode = partInfo.modulationMode;
        part.version = partInfo.version;
        if (partInfo.barcode !=  "0") {
            //非删除
            partReport.data.parts.add(part);
        }
    }
    return list;
}