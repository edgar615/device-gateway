var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setTelecontrollerInfoAck
function execute(input, logger) {

    //0成功 1失败 2正在编程     2重复添加 4未知类型 5满

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
        logger.error("control part failed: full");
        return new List();
    }
    if (input.data.result != 0) {
        logger.error("control part failed: unkown result");
    }
    logger.info("control part succeeded");
    var list = new List();
    var part = new Map();
    part.protectNo = input.data.identifyNum + 113;
    part.barcode = input.data.barcode;
    part.partType = input.data.barcode.substr(0, 5);
    part.partitionNo = input.data.partNum;
    part.battery = input.data.charge;
    //调制方式：FSK ASK ZGB，忽略
    part.modulationMode = input.data.modulationMode;
    part.version = input.data.version;

    var event = new Map();
    event.type = "report";
    event.command = "partReport";
    event.data = part;
    list.add(event);
    return list;
}