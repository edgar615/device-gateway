var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setWirelessDetectorInfoAck
function execute(input, logger) {

    //0成功 1失败 2键盘正在编程 3重复添加     4未知类型 5已满
    if (input.data.result == 0) {
        logger.info("control part succeeded");
    }
    if (input.data.result == 1) {
        logger.info("control part failed");
        return
    }
    if (input.data.result == 2) {
        logger.info("control part failed: coding");
        return
    }
    if (input.data.result == 3) {
        logger.info("control part failed: already exists");
        return
    }
    if (input.data.result == 4) {
        logger.info("control part failed: undefined type");
        return
    }
    if (input.data.result == 5) {
        logger.info("control part failed: full");
        return;
    }
    var list = new List();
    var partInfo = input.data;
    var deviceReport = new Map();
    deviceReport.type = "report";
    deviceReport.command = "deviceReport";
    deviceReport.data = new Map();
    deviceReport.data.wireLessPartCheckNum = partInfo.checknum;
    list.add(event);

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
    return list;
}