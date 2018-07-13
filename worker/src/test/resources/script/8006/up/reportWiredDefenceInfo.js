var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportWiredDefenceInfo
function execute(input, logger) {

    var list = new List();
    var checknum = input.data.checknum;
    for (var i = 0; i < input.partInfo.length; i ++) {
        var partInfo = input.partInfo[i];
        var part = new Map();
        part.protectNo = partInfo.defenceNum;
        //如果是有线防区，没有条码,平台虚拟了LH0FD
        part.productType = "LH0FD";
        part.partitionNo = partInfo.partNum;
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

        if (partInfo.actionType == 2) {
            //事件
        }
    }
    return list;
}