var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportPartInfo
function execute(input, logger) {

    var list = new List();
    var partitionList = new List();
    for (var i = 0; i < input.data.partInfo.length; i ++) {
        var partInfo = input.data.partInfo[i];
        var partition = new Map();
        partition.partitionNo = partInfo.num;
        partition.state = partInfo.open == 1 ? 1 : 2;
        partition.runningState = partInfo.alarm == 1 ? 1 : 2;
        if (partInfo.defend == 0) {
            partition.defendState = 3;
        } else if (partInfo.defend == 2) {
            partition.defendState = 1;
        }else if (partInfo.defend == 2) {
            partition.defendState = 2;
        }
        // 进入延时,报警延时
        partition.alarmDelayRunning = partInfo.inDelayStatus  == 1 ? true : false;
        partition.alarmDelay = partInfo.inDelay;
        //退出延时，布撤防延时
        partition.defendDelayRunning = partInfo.outDelayStatus  == 1 ? true : false;
        partition.defendDelay = partInfo.outDelay;
        partition.delayEnable = partInfo.inDelayEnable == 1 ||  partInfo.outDelayEnable == 1;
        partitionList.add(partition);
        var event = new Map();
        event.type = "report";
        event.command = "partitionReport";
        event.data = new Map();
        event.data.partitions = partitionList;
        list.add(event);
    }
    return list;
}