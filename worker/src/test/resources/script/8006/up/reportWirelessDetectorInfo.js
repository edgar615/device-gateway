var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessDetectorInfo消息
function execute(input, logger) {

    var list = new List();
    var checknum = input.data.checknum;
    for (var i = 0; i < input.partInfo.length; i ++) {
        var partInfo = input.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.defenceNum;
        part.barcode = partInfo.barcode;
        part.type = partInfo.type;
        part.partitionNo = partInfo.partNum;
        part.masterUnDefend = partInfo.removaValid == 1 ? true : false;
        part.masterHomeDefend = partInfo.atHomeValid == 1 ? true : false;
        part.masterAwayDefend = partInfo.awayValid == 1 ? true : false;
        //阀值
        part.threshold = partInfo.status == 1 ? true : false;
        //拆动
        part.dismantle = partInfo.tamperStatus == 1 ? true : false;
        //开启离线检测，出厂默认开启
        part.offlineSwitch = partInfo.loseDetectSwitch == 1 ? true : false;
        part.offline = partInfo.loseStatus == 1 ? true : false;
        //警号
        part.alarmSwitch = partInfo.alarmSwitch == 1 ? true : false;
        //迎宾，门磁特有
        part.welcomeSwitch = partInfo.usherSwitch == 1 ? true : false;
        part.battery = partInfo.charge;
        part.signal = partInfo.signal;
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