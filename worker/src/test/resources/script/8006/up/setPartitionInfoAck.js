var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setPartitionInfoAck
function execute(input, logger) {

    var result = input.data.result;
    if (result == 1) {
        logger.error("setPartitionInfo failed: invalid arg");
        return new List();
    }
    if (result == 2) {
        logger.error("setPartitionInfo failed: coding");
        return new List();
    }
    if (result == 5) {
        logger.error("setPartitionInfo failed: not synchronized");
        return new List();
    }
    if (result != 0) {
        logger.error("setPartitionInfo failed: unknown result");
        return new List();
    }
    logger.info("setPartitionInfo succeeded");
    var list = new List();
    var partition = new Map();
    partition.partitionNo = input.data.num;
    partition.state = input.data.open == 1 ? 1 : 2;
    partition.runningState = input.data.alarm == 1 ? 2 : 1;
    partition.alarmDelayRunning = input.data.inDelayStatus == 1;
    partition.defendDelayRunning = input.data.outDelayStatus == 1;
    if (input.data.defend == 0) {
        partition.defendState = 3;
    }
    if (input.data.defend == 1) {
        partition.defendState = 1;
    }
    if (input.data.defend == 2) {
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
    return list;
}