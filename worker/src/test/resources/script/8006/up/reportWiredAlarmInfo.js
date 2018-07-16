var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWiredAlarmInfo
function execute(input, logger) {

    var list = new List();
    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = new Map();
    partReport.data.parts = new List();
    list.add(partReport);

    var checknum = input.data.checknum;
    //98内置警号 99外接有线警号 100~106 无线警号
    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.defenceNum + 98;
        part.barcode = partInfo.barcode;
        part.sirenDuration = partInfo.alarmTime;
        part.runningState = partInfo.alarmStatus;
        part.sirenSwitch = partInfo.enable  == 1;

        //调制方式：FSK ASK ZGB，忽略
        part.modulationMode = partInfo.modulationMode;
        part.version = partInfo.version;
        partReport.data.parts.add(part);
    }
    return list;
}