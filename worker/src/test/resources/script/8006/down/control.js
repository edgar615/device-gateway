var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceControl
function execute(input, logger) {

    var list = new List();
    var control = new Map();
    control.type = "control";
    control.command = "setPartitionInfo";
    control.partInfo = new List();
    list.add(control);
    var partitionControl = new Map();
    partitionControl.num = input.data.partitionNo;
    if (input.data.state != undefined) {
        partitionControl.open = input.data.state == 1 ? 1 : 0;
    }
    if (input.data.delayEnable != undefined) {
        partitionControl.inDelayEn = input.data.delayEnable ? 1 : 0;
        partitionControl.outDelayEn = input.data.delayEnable ? 1 : 0;
    }
    if (input.data.defendDelay != undefined) {
        partitionControl.inDelay = input.data.defendDelay ? 1 : 0;
    }
    if (input.data.alarmDelay != undefined) {
        partitionControl.outDelay = input.data.alarmDelay ? 1 : 0;
    }
    //if (input.data.defendState != undefined) {
    //    var defendState = input.data.defendState;
    //    if (defendState == 1) {
    //        partitionControl.defend = 1;
    //    }
    //    if (defendState == 2) {
    //        partitionControl.defend = 2;
    //    }
    //    if (defendState == 0) {
    //        partitionControl.defend = 3;
    //    }
    //}
    return list;
}