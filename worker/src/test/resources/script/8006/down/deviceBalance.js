var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceBalance
function execute(input, logger) {

    var actual = input.data.actual;
    var desired = input.data.desired;

    //0所有分区，1无线探测器，2有线防区，3无线遥控器，4 无线警号，5 有线警号，6 RFID, 7无线PGM ,8 有线PGM
    var syncInfoList = new List();
    var syncInfo = new Map();
    syncInfo.deviceType = 0;
    syncInfo.syncFlag = actual.partitionCheckNum == desired.partitionCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo)
    var syncInfo = new Map();
    syncInfo.deviceType = 1;
    syncInfo.syncFlag = actual.wireLessPartCheckNum == desired.wireLessPartCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo);
    var syncInfo = new Map();
    syncInfo.deviceType = 2;
    syncInfo.syncFlag = actual.wirePartCheckNum == desired.wirePartCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo)
    var syncInfo = new Map();
    syncInfo.deviceType = 3;
    syncInfo.syncFlag = actual.wireLessControlCheckNum == desired.wireLessControlCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo);
    var syncInfo = new Map();
    syncInfo.deviceType = 4;
    syncInfo.syncFlag = actual.wireLessAlarmCheckNum == desired.wireLessAlarmCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo);
    var syncInfo = new Map();
    syncInfo.deviceType = 5;
    syncInfo.syncFlag = actual.wireAlarmCheckNum == desired.wireAlarmCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo);
    var syncInfo = new Map();
    syncInfo.deviceType = 6;
    syncInfo.syncFlag = actual.rfidCheckNum == desired.rfidCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo);
    var syncInfo = new Map();
    syncInfo.deviceType = 7;
    syncInfo.syncFlag = actual.wireLessPgmCheckNum == desired.wireLessPgmCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo);
    var syncInfo = new Map();
    syncInfo.deviceType = 8;
    syncInfo.syncFlag = actual.wirePgmCheckNum == desired.wirePgmCheckNum ? 0 : 1;
    syncInfoList.add(syncInfo);
    var list = new List();
    var control = new Map();
    control.type = "control";
    control.command = "syncDeviceInfo";
    control.data = new Map();
    control.data.syncInfo = syncInfoList;
    list.add(control);

    return list;
}
