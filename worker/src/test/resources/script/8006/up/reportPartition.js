var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportPartInfo消息
function execute(input, logger) {

    var list = new List();
    for (var i = 0; i < input.partInfo.length; i ++) {
        var partInfo = input.partInfo[i];
        var partition = new Map();
        partition.partitionNo = partInfo.num;
        partition.state = partInfo.open == 1 ? 1 : 2;
        partition.runningState = partInfo.alarm == 1 ? 1 : 2;
        if (partInfo.defend == 1) {
            partition.defendState = 3;
        } else if (partInfo.defend == 2) {
            partition.defendState = 1;
        }else if (partInfo.defend == 3) {
            partition.defendState = 2;
        }
        // 进入延时,报警延时
        partition.alarmDelayRunning = partInfo.inDelayStatus  == 1 ? true : false;
        partition.alarmDelay = partInfo.inDelay;
        //退出延时，布撤防延时
        partition.defendDelayRunning = partInfo.outDelayStatus  == 1 ? true : false;
        partition.defendDelay = partInfo.outDelay;

        var event = new Map();
        event.type = "report";
        event.command = "deviceReport";
        event.data = partition;
        list.add(event);
    }
    return list;
}