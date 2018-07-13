var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessDetectorInfo
function execute(input, logger) {

    var list = new List();
    //上报无线探测器校验和
    var deviceReport = new Map();
    deviceReport.type = "report";
    deviceReport.command = "deviceReport";
    deviceReport.data = new Map();
    deviceReport.data.wireLessPartCheckNum = input.data.checknum;
    list.add(deviceReport);
    //64位字符串，对应1-64是否已经注册，每一位1表示已注册，0没有注册
    var isRegst = input.data.isRegst;
    var deletedParts = new List();
    for (var i = 0; i < 64; i ++) {
        if (isRegst.charAt(i) == 0) {
            deletedParts.add(i);
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

        var partReport = new Map();
        partReport.type = "report";
        partReport.command = "partReport";
        partReport.data = part;
        list.add(partReport);

        if (partInfo.actionType == 2) {
            var event = new Map();
            event.type = "event";
            event.command = "new";
            event.data = new Map();
            //时间转换
            //event.data.time = time + timezone * 60 * 60;
            event.data.level = 1;
            event.data.push = false;
            event.data.defend = false;
            event.data.protectNo = partInfo.defenceNum;
            list.add(event);
        }
    }
    return list;
}