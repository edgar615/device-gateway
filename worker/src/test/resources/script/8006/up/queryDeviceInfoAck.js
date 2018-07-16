var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up queryDeviceInfoAck
function execute(input, logger) {
    var list = new List();
    var report = new Map();
    report.type = "report";
    report.command = "deviceReport";
    report.data = new Map();
    list.add(report);

    for (var i = 0; i < input.checksums.length; i++) {
        var checksumData = input.checksums[i];
        //0所有分区，1无线探测器，2有线防区，3无线遥控器，4 无线警号，5 有线警号，6 RFID, 7无线PGM ,8 有线PGM
        var deviceType = checksumData.deviceType;
        var result = checksumData.result;
        var checkNum = checksumData.checknum;
        if (deviceType == 0) {
            report.data.partitionCheckNum = checkNum;
        }
        if (deviceType == 1) {
            report.data.wireLessPartCheckNum = checkNum;
        }
        if (deviceType == 2) {
            report.data.wirePartCheckNum = checkNum;
        }
        if (deviceType == 3) {
            report.data.wireLessControlCheckNum = checkNum;
        }
        if (deviceType == 4) {
            report.data.wireLessAlarmCheckNum = checkNum;
        }
        if (deviceType == 5) {
            report.data.wireAlarmCheckNum = checkNum;
        }
        if (deviceType == 6) {
            report.data.rfidCheckNum = checkNum;
        }
        if (deviceType == 7) {
            report.data.wireLessPgmCheckNum = checkNum;
        }
        if (deviceType == 8) {
            report.data.wirePgmCheckNum = checkNum;
        }
    }
    return list;
}