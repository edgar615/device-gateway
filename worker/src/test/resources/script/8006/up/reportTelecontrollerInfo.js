var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up reportTelecontrollerInfo
function execute(input, logger) {

    var list = new List();
    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = new Map();
    partReport.data.parts = new List();
    list.add(partReport);

    //0同歩 1添加/删除 2上报遥控器状态变化
    var actionType = input.data.actionType;
    //113~144 无线遥控器
    var partRegistrySync = new Map();
    partRegistrySync.type = "report";
    partRegistrySync.command = "partRegistrySync";
    partRegistrySync.data = new Map();
    partRegistrySync.data.startIndex = 113;
    partRegistrySync.data.endIndex = 144;
    partRegistrySync.data.registryType = "wirelessControl";
    partRegistrySync.data.partRegistry = input.data.isRegst;
    list.add(partRegistrySync);

    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.protectNo = new Integer(partInfo.identifyNum + 113);
        part.barcode = partInfo.barcode;
        part.partType = partInfo.barcode.substr(0,5);
        part.partitionNo = partInfo.partNum;
        part.battery = partInfo.charge;
        //调制方式：FSK ASK ZGB，忽略
        part.modulationMode = partInfo.modulationMode;
        part.version = partInfo.version;
        if (partInfo.barcode !=  "0") {
            //非删除
            partReport.data.parts.add(part);
        }

    }
    return list;
}