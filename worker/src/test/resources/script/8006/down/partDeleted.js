var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");
//down partDeleted
function execute(input, logger) {

    var list = new List();
    if (input.data.protectNo >= 0 && input.data.protectNo <= 63) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessDetectorInfo";
        control.data = new Map();
        control.data.defenceNum = input.data.protectNo;
        control.data.actionType = 1;
        control.data.barcode = "0";
        list.add(control);
    }
    if (input.data.protectNo >= 100 && input.data.protectNo <= 106) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessAlarmInfo";
        control.data = new Map();
        control.data.actionType = 1;
        control.data.barcode = "0";
        control.data.identifyNum = new Integer(input.data.protectNo - 100);
        list.add(control);
    }
    if (input.data.protectNo >= 108 && input.data.protectNo <= 112) {
        var control = new Map();
        control.type = "control";
        control.data = new Map();
        control.command = "setWirelessPGM";
        control.data.actionType = 1;
        control.data.identifyNum = new Integer(input.data.protectNo - 108);
    }
    if (input.data.protectNo >= 113 && input.data.protectNo <= 144) {
        var control = new Map();
        control.type = "control";
        control.command = "setTelecontrollerInfo";
        control.data = new Map();
        control.data.actionType = 1;
        control.data.barcode = "0";
        control.data.identifyNum = new Integer(input.data.protectNo - 113);
        list.add(control);
    }
    return list;
}
