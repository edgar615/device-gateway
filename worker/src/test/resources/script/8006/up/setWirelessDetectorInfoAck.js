var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setWirelessDetectorInfoAck
function execute(input, logger) {

    //0成功 1失败 2键盘正在编程 3重复添加     4未知类型 5已满
    if (input.data.result == 1) {
        logger.error("control part failed");
        return new List();
    }
    if (input.data.result == 2) {
        logger.error("control part failed: coding");
        return new List();
    }
    if (input.data.result == 3) {
        logger.error("control part failed: already exists");
        return new List();
    }
    if (input.data.result == 4) {
        logger.error("control part failed: undefined type");
        return new List();
    }
    if (input.data.result == 5) {
        logger.error("control part failed: synchronizing");
        return new List();
    }
    if (input.data.result == 6) {
        logger.error("control part failed: full");
        return new List();
    }
    if (input.data.result != 0) {
        logger.error("control part failed: unkown result");
        return new List();
    }
    if (input.data.barcode ==  "0") {
        logger.info("delete part succeeded");
        var list = new List();
        var partDeleted = new Map();
        partDeleted.type = "report";
        partDeleted.command = "partDeleted";
        partDeleted.data = new Map();
        partDeleted.data.protectNo = input.data.defenceNum;
        list.add(partDeleted);
        return list;
    }
    logger.info("control part succeeded");
    var list = new List();
    var deviceReport = new Map();
    deviceReport.type = "report";
    deviceReport.command = "deviceReport";
    deviceReport.data = new Map();
    deviceReport.data.wireLessPartCheckNum = input.data.checknum;
    list.add(deviceReport);

    var part = new Map();
    part.protectNo = input.data.defenceNum;
    part.barcode = input.data.barcode;
    part.partType = input.data.barcode.substr(0, 5);
    part.partitionNo = input.data.partNum;
    part.masterUnDefend = input.data.removaValid == 1 ? true : false;
    part.masterHomeDefend = input.data.atHomeValid == 1 ? true : false;
    part.masterAwayDefend = input.data.awayValid == 1 ? true : false;
    //阀值
    part.threshold = input.data.status == 1 ? true : false;
    //拆动
    part.dismantle = input.data.tamperStatus == 1 ? true : false;
    //开启离线检测，出厂默认开启
    part.offlineSwitch = input.data.loseDetectSwitch == 1 ? true : false;
    part.offline = input.data.loseStatus == 1 ? true : false;
    //警号
    part.alarmSwitch = input.data.alarmSwitch == 1 ? true : false;
    //迎宾，门磁特有
    part.welcomeSwitch = input.data.usherSwitch == 1 ? true : false;
    part.battery = input.data.charge;
    part.signal = input.data.signal;
    //调制方式：FSK ASK ZGB，忽略
    part.modulationMode = input.data.modulationMode;
    part.version = input.data.version;
    //名称
    if (input.data.name != undefined) {
        part.unicodeName = "";
        for (var j = 0; j < input.data.name.length; j ++) {
            part.unicodeName += "\\u" + input.data.name[j];
        }
    }
    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = part;
    list.add(partReport);
    return list;
}