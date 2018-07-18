var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partitionControl
function execute(input, logger) {

    var control = new Map();
    control.type = "control";
    control.command = "setPartitionInfo";
    var partitionControl = new Map();
    control.data = partitionControl;
    var flag = false;
    partitionControl.num = input.data.partitionNo;
    if (input.data.state != undefined) {
        partitionControl.open = input.data.state == 1 ? 1 : 0;
        flag = true;
    }
    if (input.data.delayEnable != undefined) {
        partitionControl.inDelayEn = input.data.delayEnable ? 1 : 0;
        partitionControl.outDelayEn = input.data.delayEnable ? 1 : 0;
        flag = true;
    }
    if (input.data.defendDelay != undefined) {
        partitionControl.inDelay = input.data.defendDelay;
        flag = true;
    }
    if (input.data.alarmDelay != undefined) {
        partitionControl.outDelay = input.data.alarmDelay;
        flag = true;
    }
    var list = new List();
    if (flag) {
        list.add(control);
    }
    return list;
}