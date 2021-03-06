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
        part.protectNo = partInfo.identifyNum == 0 ? 98 : 99;
        part.partType = "LH0FC";
        part.sirenDuration = partInfo.alarmTime;
        part.runningState = partInfo.alarmStatus;
        if (partInfo.enable == true || partInfo.enable == 1) {
            part.sirenSwitch = true;
        } else {
            part.sirenSwitch = false;
        }
        partReport.data.parts.add(part);
        if (partInfo.identifyNum == 0) {
            //同步更新设备的警笛时长
            var deviceReport = new Map();
            deviceReport.type = "report";
            deviceReport.command = "deviceReport";
            deviceReport.data = new Map();
            deviceReport.data.sirenDuration = partInfo.alarmTime;
            list.add(deviceReport);
        }
    }
    return list;
}