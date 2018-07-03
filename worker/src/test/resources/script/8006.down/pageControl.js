var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partControl
function execute(input, logger) {

    var list = new List();

    wirelessPart(input, logger, list);

    return list;
}

function wirelessPart(input, logger, list) {
    if (input.data.protectNo > 0 && input.data.protectNo <= 64) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessDetectorInfo";
        control.data = new Map();
        list.add(control);
        control.actionType = 0;
        control.defenceNum = input.data.protectNo;
        if (input.data.partitionNo != undefined) {
            control.partNum = input.data.partitionNo;
        }
        if (input.data.masterUnDefend != undefined) {
            control.removaValid = input.data.masterUnDefend ? 1 : 0;
        }
        if (input.data.masterAwayDefend != undefined) {
            control.awayValid = input.data.masterAwayDefend ? 1 : 0;
        }
        if (input.data.masterHomeDefend != undefined) {
            control.atHomeValid = input.data.masterHomeDefend ? 1 : 0;
        }
        if (input.data.offlineSwitch != undefined) {
            control.loseDetectSwitch = input.data.offlineSwitch ? 1 : 0;
        }
        //门磁特有
        if (input.data.welcomeSwitch != undefined) {
            control.usherSwitch = input.data.welcomeSwitch ? 1 : 0;
        }
    }
}

function wirePart(input, logger, list) {
    if (input.data.protectNo > 64 && input.data.protectNo <= 64 + 32) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessDetectorInfo";
        control.data = new Map();
        list.add(control);
        control.actionType = 0;
        control.defenceNum = input.data.protectNo - 64;
        if (input.data.partitionNo != undefined) {
            control.partNum = input.data.partitionNo;
        }
        if (input.data.masterUnDefend != undefined) {
            control.removaValid = input.data.masterUnDefend ? 1 : 0;
        }
        if (input.data.masterAwayDefend != undefined) {
            control.awayValid = input.data.masterAwayDefend ? 1 : 0;
        }
        if (input.data.masterHomeDefend != undefined) {
            control.atHomeValid = input.data.masterHomeDefend ? 1 : 0;
        }
    }
}