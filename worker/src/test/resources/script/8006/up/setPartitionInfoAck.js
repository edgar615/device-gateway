var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setPartitionInfoAck
function execute(input, logger) {

    var result = input.data.result;
    if (result == 1) {
        logger.error("setPartitionInfoAck failed: invalid arg");
    } else if (result == 2) {
        logger.error("setPartitionInfoAck failed: coding");
    } else {
        logger.info("setPartitionInfoAck succeeded");
    }
    var list = new List();
    for (var i = 0; i < input.data.partInfo.length; i++) {
        var part = input.data.partInfo[i];
        var partition = new Map();
        partition.partitionNo = part.num;
        partition.state = part.open == 1 ? 1 : 2;
        partition.runningState = part.alarm == 1 ? 2 : 1;
        partition.alarmDelayRunning = part.inDelayStatus == 1;
        partition.defendDelayRunning = part.outDelayStatus == 1;
        if (part.defend == 0) {
            partition.defendState = 3;
        }
        if (part.defend == 1) {
            partition.defendState = 1;
        }
        if (part.defend == 2) {
            partition.defendState = 2;
        }
        partition.delayEnable = input.data.inDelayEn == 1 || input.data.outDelayEn == 1;
        partition.alarmDelay = input.data.inDelay;
        partition.defendDelay = input.data.outDelay;
        var event = new Map();
        event.type = "report";
        event.command = "partitionReport";
        event.data = partition;
        list.add(event);
    }
    return list;
}