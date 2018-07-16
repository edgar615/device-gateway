var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWirelessDetectorInfo
function execute(input, logger) {

    var list = new List();
    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = new Map();
    partReport.data.parts = new List();
    list.add(partReport);

    //上报无线探测器校验和
    //var deviceReport = new Map();
    //deviceReport.type = "report";
    //deviceReport.command = "deviceReport";
    //deviceReport.data = new Map();
    //deviceReport.data.wireLessPartCheckNum = input.data.checknum;
    //list.add(deviceReport);
    //64位字符串，对应1-64是否已经注册，每一位1表示已注册，0没有注册
    var partRegistrySync = new Map();
    partRegistrySync.type = "report";
    partRegistrySync.command = "partRegistrySync";
    partRegistrySync.data = new Map();
    partRegistrySync.data.startIndex = 113;
    partRegistrySync.data.endIndex = 144;
    partRegistrySync.data.registryType = "wirelessDetector";
    partRegistrySync.data.partRegistry = input.data.isRegst;
    list.add(partRegistrySync);

    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.defenceNum;
        part.barcode = partInfo.barcode;
        part.partType = partInfo.barcode.substr(0, 5);
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

        partReport.data.parts.add(part);

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