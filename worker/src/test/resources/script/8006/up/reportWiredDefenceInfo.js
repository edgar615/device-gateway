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
        //名称
        if (partInfo.name != undefined) {
            part.unicodeName = "";
            for (var j = 0; j < partInfo.name.length; j ++) {
                part.unicodeName += "\\u" + partInfo.name[j];
            }
        }
        partReport.data.parts.add(part);

        //有线防区同步激活配件
        if (input.data.actionType == 2) {
            var partActive = new Map();
            partActive.type = "report";
            partActive.command = "partActive";
            partActive.data = new Map();
            partActive.data.partType = "LH0FD";
            partActive.data.protectNo = partInfo.defenceNum;
            list.add(partActive);
        }
    }
    return list;
}