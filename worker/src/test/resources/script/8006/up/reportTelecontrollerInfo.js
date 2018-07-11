var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportTelecontrollerInfo消息
function execute(input, logger) {

    var list = new List();
    //0同歩 1添加/删除 2上报遥控器状态变化
    var actionType = input.data.actionType;
    var isRegst = input.data.isRegst;
    //todo 更新
    for (var i = 0; i < input.partInfo.length; i ++) {
        var partInfo = input.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.identifyNum;
        part.barcode = partInfo.barcode;
        part.partitionNo = partInfo.partNum;
        part.battery = partInfo.charge;
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