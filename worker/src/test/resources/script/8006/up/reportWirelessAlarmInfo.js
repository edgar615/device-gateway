var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessAlarmInfo
function execute(input, logger) {

    var list = new List();
    var checknum = input.data.checknum;
    var isRegst = input.data.isRegst;
    //98内置警号 99外接有线警号 100~106 无线警号
    var deletedParts = new List();
    for (var i = 0; i < 7; i ++) {
        if (isRegst.charAt(i) == 0) {
            deletedParts.add(i + 100);
        }
    }
    if (deletedParts.length > 0) {
        var deletedPartSync = new Map();
        deletedPartSync.type = "report";
        deletedPartSync.command = "deletedPartSync";
        deletedPartSync.data = new Map();
        deletedPartSync.data.parts = deletedParts;
        list.add(deletedPartSync);
    }

    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.defenceNum + 100;
        part.barcode = partInfo.barcode;
        part.partitionNo = partInfo.partNum;
        part.sirenDuration = partInfo.alarmTime;
        part.runningState = partInfo.alarmStatus;
        part.lightSwitch = partInfo.alarmLamp == 1;
        part.sirenSwitch = partInfo.enabled == 1;
        part.sirenVolume = partInfo.alarmVolume

        //调制方式：FSK ASK ZGB，忽略
        part.modulationMode = partInfo.modulationMode;
        part.version = partInfo.version;

        var event = new Map();
        event.type = "report";
        event.command = "partReport";
        event.data = part;
        list.add(event);
    }
    return list;
}