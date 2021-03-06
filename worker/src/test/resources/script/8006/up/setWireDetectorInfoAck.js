var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setWireDetectorInfoAck
function execute(input, logger) {

    if (input.data.result == 1) {
        logger.error("control part failed");
        return new List();
    }
    if (input.data.result == 2) {
        logger.error("control part failed: coding");
        return new List();
    }
    if (input.data.result == 5) {
        logger.error("control part failed: full");
        return new List();
    }
    if (input.data.result != 0) {
        logger.error("control part failed: unkown result");
    }

    var list = new List();
    var partInfo = input.data;
    var part = new Map();
    part.partType = "LH0FD";
    part.partitionNo = partInfo.partNum;
    part.protectNo = partInfo.defenceNum;
    part.masterUnDefend = partInfo.removaValid == 1 ? true : false;
    part.masterHomeDefend = partInfo.atHomeValid == 1 ? true : false;
    part.masterAwayDefend = partInfo.awayValid == 1 ? true : false;
    //阀值
    part.threshold = partInfo.status == 1 ? true : false;
    //名称
    if (partInfo.name != undefined) {
        part.unicodeName = "";
        for (var j = 0; j < partInfo.name.length; j ++) {
            part.unicodeName += "\\u" + partInfo.name[j];
        }
    }
    var event = new Map();
    event.type = "report";
    event.command = "partReport";
    event.data = part;
    list.add(event);
    return list;
}