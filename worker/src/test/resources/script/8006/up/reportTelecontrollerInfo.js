var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportTelecontrollerInfo
function execute(input, logger) {

    var list = new List();
    //0同歩 1添加/删除 2上报遥控器状态变化
    var actionType = input.data.actionType;
    var isRegst = input.data.isRegst;
    //113~144 无线遥控器
    var deletedParts = new List();
    for (var i = 0; i < 32; i ++) {
        if (isRegst.charAt(i) == 0) {
            deletedParts.add(i + 113);
        }
    }
    if (deletedParts.length > 0) {
        var deletedPartSync = new Map();
        deletedPartSync.type = "report";
        deletedPartSync.command = "deletedPartSync";
        deletedPartSync.data = new Map();
        deletedPartSync.data.parts = deletedParts;
        list.add(deletedPartSync);
    }

    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.identifyNum + 113;
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