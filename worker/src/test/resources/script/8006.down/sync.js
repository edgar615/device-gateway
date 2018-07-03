var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceSync消息
function execute(input, logger) {

    var list = new List();
    var checksumList = new List();
    //0所有分区，1无线探测器，2有线防区，3无线遥控器，4 无线警号，5 有线警号，6 RFID, 7无线PGM ,8 有线PGM
    var checksum = new Map();
    checksum.deviceType = 0;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 1;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 2;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 3;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 4;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 5;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 6;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 7;
    checksum.checknum = 0;
    checksumList.add(checksum);
    checksum = new Map();
    checksum.deviceType = 8;
    checksum.checknum = 0;
    checksumList.add(checksum);

    var control = new Map();
    control.type = "control";
    control.command = "queryDeviceInfo";
    control.data = new Map();
    event.data.checksums = checksums;
    list.add(control);

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

    return list;
}
