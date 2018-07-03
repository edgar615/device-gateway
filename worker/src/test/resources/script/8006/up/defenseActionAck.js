var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up defenseActionAck消息
function execute(input, logger) {

    var result = input.data.result;
    if (result == 1) {
        logger.error("defenseAction failed: no this partition");
        return;
    }
    if (result == 2) {
        logger.error("defenseAction failed: coding");
        return;
    }
    logger.info("defenseAction succeeded");
    for (var i = 0; i < input.partInfo.length; i++) {
        var part = input.partInfo[i];
        var partitionNo = part.partNum;
        //0：分区未开启 1撤防  2 外出布防 3 留守布防
        var partState = part.partState;
        //todo
    }
}