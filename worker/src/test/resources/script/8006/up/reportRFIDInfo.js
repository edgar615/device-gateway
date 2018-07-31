var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//up reportRFIDInfo
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
    //deviceReport.data.rfidCheckNum = input.data.checknum;
    //list.add(deviceReport);

    //0同歩 1添加/删除 2上报遥控器状态变化
    var actionType = input.data.actionType;

    //同步注册表
    var isRegst = input.data.isRegst;
    var partRegistrySync = new Map();
    partRegistrySync.type = "report";
    partRegistrySync.command = "partRegistrySync";
    partRegistrySync.data = new Map();
    partRegistrySync.data.startIndex = 145;
    partRegistrySync.data.endIndex = 176;
    partRegistrySync.data.registryType = "rfid";
    partRegistrySync.data.partRegistry = input.data.isRegst;
    list.add(partRegistrySync);

    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.partType = "LH0FF";
        part.protectNo = new Integer(partInfo.identifyNum + 145);
        part.barcode = partInfo.cardID;

        //控制类型 0布撤防 1PGM
        part.rfidCtrlType = new Integer(partInfo.ctrlType + 1);
        if (rfidCtrlType == 1) {
            var controlPartition = new List();
            for (var i = 1; i < 9; i ++) {
                if ((input.data.partNum & i) == 1) {
                    controlPartition.add(new Integer(i - 1));
                }
            }
            part.controlPartition = controlPartition;
        }
        if (rfidCtrlType == 2) {
            //控制类型为PGM时有效，bit0-bit5对应有线PGM和无线PGM1-5,对应位1表示控制
            var controlPgm = new List();
            for (var i = 1; i < 7; i ++) {
                if ((partInfo.PGM & i) == 1) {
                    controlPgm.add(new Integer(i - 1));
                }
            }
            part.controlPgm = controlPgm;
        }
        //名称
        if (partInfo.name != undefined) {
            part.unicodeName = "";
            for (var j = 0; j < partInfo.name.length; j ++) {
                part.unicodeName += "\\u" + partInfo.name[j];
            }
        }
        partReport.data.parts.add(part);
    }
    return list;
}