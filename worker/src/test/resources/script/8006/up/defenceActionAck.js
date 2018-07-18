var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up defenceActionAck
function execute(input, logger) {

    var result = input.data.result;
    if (result == 1) {
        logger.error("defenseAction failed: no this partition");
        return new List();
    }
    if (result == 2) {
        logger.error("defenseAction failed: coding");
        return new List();
    }
    if (result == 3) {
        logger.error("defenseAction failed: not synchronized");
        return new List();
    }
    if (result == 4) {
        logger.error("defenseAction failed: invalid args");
        return new List();
    }
    if (result != 0) {
        logger.error("defenseAction failed: unknown result");
        return new List();
    }
    logger.info("defenseAction succeeded");
    var partitionList = new List();
    for (var i = 0; i < input.data.partInfo.length; i++) {
        var part = input.data.partInfo[i];
        var partitionReport = new Map();
        partitionList.add(partitionReport);
        partitionReport.partitionNo = part.partNum;
        //0：分区未开启 1撤防  2 外出布防 3 留守布防
        var partState = part.partState;
        partitionReport.state = 1;
        if (partState == 3) {
            partitionReport.state = 2;
        } else if (partState == 0) {
            partitionReport.defendState = 3;
        } else if (partState == 1) {
            partitionReport.defendState = 1;
        } else if (partState == 2) {
            partitionReport.defendState = 2;
        } else {
            logger.error("undefined defend:" + partState);
        }
    }
    var list = new List();
    var report = new Map();
    report.type = "report";
    report.command = "partitionReport";
    report.data = new Map();
    report.data.partitions = partitionList;
    list.add(report);
    return list;
}