var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceSync消息
function execute(input, logger) {

    var list = new List();
    var syncInfoList = new List();
    //0所有分区，1无线探测器，2有线防区，3无线遥控器，4 无线警号，5 有线警号，6 RFID, 7无线PGM ,8 有线PGM
    var syncInfo = new Map();
    syncInfo.deviceType = 0;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 1;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 2;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 3;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 4;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 5;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 6;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 7;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    syncInfo = new Map();
    syncInfo.deviceType = 8;
    syncInfo.syncFlag = 1;
    syncInfoList.add(syncInfo);
    var syncDevice = new Map();
    syncDevice.type = "control";
    syncDevice.command = "syncDeviceInfo";
    syncDevice.data = new Map();
    syncDevice.data.syncInfo = syncInfoList;
    list.add(syncDevice);

    return list;
}
