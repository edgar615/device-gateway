var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setWireDetectorInfoAck
function execute(input, logger) {

    var list = new List();
    var partInfo = input.data;
    var part = new Map();
    part.partType = "LH0FD";
    part.partitionNo = partInfo.defenceNum;
    part.protectNo = partInfo.partNum;
    part.masterUnDefend = partInfo.removaValid == 1 ? true : false;
    part.masterHomeDefend = partInfo.atHomeValid == 1 ? true : false;
    part.masterAwayDefend = partInfo.awayValid == 1 ? true : false;
    //阀值
    part.threshold = partInfo.status == 1 ? true : false;

    var event = new Map();
    event.type = "report";
    event.command = "partReport";
    event.data = part;
    list.add(event);
    return list;
}