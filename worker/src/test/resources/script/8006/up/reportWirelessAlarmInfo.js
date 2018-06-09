var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessAlarmInfo消息
function execute(input, logger) {

    var list = new List();
    var checknum = input.data.checknum;
    for (var i = 0; i < input.partInfo.length; i ++) {
        var partInfo = input.partInfo[i];
        var part = new Map();
        //todo 有问题
        part.protectNo = partInfo.defenceNum;
        part.barcode = partInfo.barcode;
        part.partitionNo = partInfo.partNum;
        part.sirenDuration = partInfo.alarmTime;
        part.runningState = partInfo.alarmStatus;
        part.lightSwitch = partInfo.alarmLamp;
        part.sirenSwitch = partInfo.warningSign;
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