var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWiredDefenceInfo
function execute(input, logger) {

    var list = new List();
    var partReport = new Map();
    partReport.type = "report";
    partReport.command = "partReport";
    partReport.data = new Map();
    partReport.data.parts = new List();
    list.add(partReport);
    var checknum = input.data.checknum;
    for (var i = 0; i < input.data.partInfo.length; i++) {
        var partInfo = input.data.partInfo[i];
        var part = new Map();
        part.partType = "LH0FD";
        part.partitionNo = partInfo.partNum;
        part.protectNo = partInfo.defenceNum;
        part.masterUnDefend = partInfo.removaValid == 1 ? true : false;
        part.masterHomeDefend = partInfo.atHomeValid == 1 ? true : false;
        part.masterAwayDefend = partInfo.awayValid == 1 ? true : false;
        //阀值
        part.threshold = partInfo.status == 1 ? true : false;
        partReport.data.parts.add(part);

        if (partInfo.actionType == 2) {
            //事件
        }
    }
    return list;
}