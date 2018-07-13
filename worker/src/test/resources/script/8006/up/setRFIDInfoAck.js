var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setRFIDInfoAck
function execute(input, logger) {
    var result = input.data.result;
    if (result == 1) {
        logger.error("setPartitionInfoAck failed: invalid arg");
    } else if (result == 2) {
        logger.error("setPartitionInfoAck failed: coding");
    } else {
        logger.info("setPartitionInfoAck succeeded");
    }

    var part = new Map();
    part.protectNo = input.data.identifyNum + 145;
    part.barcode =  input.data.cardID;

    //控制类型 0布撤防 1PGM
    part.rfidCtrlType =  input.data.ctrlType + 1;
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
            if ((input.data.PGM & i) == 1) {
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
    return list;
}