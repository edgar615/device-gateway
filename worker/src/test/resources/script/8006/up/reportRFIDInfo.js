var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportRFIDInfo
function execute(input, logger) {

    var list = new List();
    //上报无线探测器校验和
    var deviceReport = new Map();
    deviceReport.type = "report";
    deviceReport.command = "deviceReport";
    deviceReport.data = new Map();
    deviceReport.data.rfidCheckNum = input.data.checknum;
    list.add(deviceReport);

    //0同歩 1添加/删除 2上报遥控器状态变化
    var actionType = input.data.actionType;
    var isRegst = input.data.isRegst;
    //145~176 RFID
    var deletedParts = new List();
    for (var i = 0; i < 32; i ++) {
        if (isRegst.charAt(i) == 0) {
            deletedParts.add(i + 145);
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
        part.partType = "LH0FF";
        part.protectNo = partInfo.identifyNum + 145;
        part.barcode = partInfo.cardID;

        //控制类型 0布撤防 1PGM
        part.rfidCtrlType = partInfo.ctrlType + 1;
        if (rfidCtrlType == 1) {
            var controlPartition = new List();
            for (var i = 1; i < 9; i ++) {
                if ((input.data.partNum & i) == 1) {
                    controlPartition.add(i - 1);
                }
            }
            part.controlPartition = controlPartition;
        }
        if (rfidCtrlType == 2) {
            //控制类型为PGM时有效，bit0-bit5对应有线PGM和无线PGM1-5,对应位1表示控制
            var controlPgm = new List();
            for (var i = 1; i < 7; i ++) {
                if ((partInfo.PGM & i) == 1) {
                    controlPgm.add(i - 1);
                }
            }
            part.controlPgm = controlPgm;
        }
        var event = new Map();
        event.type = "report";
        event.command = "rfidReport";
        event.data = part;
        list.add(event);

    }
    return list;
}