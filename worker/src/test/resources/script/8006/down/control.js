var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceControl
function execute(input, logger) {

    var control = new Map();
    control.type = "control";
    control.command = "setPartitionInfo";
    control.partInfo = new List();
    var partitionControl = new Map();
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
        partitionControl.inDelay = input.data.defendDelay ? 1 : 0;
        flag = true;
    }
    if (input.data.alarmDelay != undefined) {
        partitionControl.outDelay = input.data.alarmDelay ? 1 : 0;
        flag = true;
    }
    var list = new List();
    if (flag) {
        list.add(control);
    }
    return list;
}