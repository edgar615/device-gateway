var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partAdded
function execute(input, logger) {

    var list = new List();
    var partType = input.data.barcode.substr(0, 5);
    if (partType == "LH095") {
        //无线警号
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessAlarmInfo";
        control.data = new Map();
        control.data.identifyNum = 255;
        control.data.actionType = 1;
        control.data.barcode = input.data.barcode;
        control.data.partNum = input.data.partitionNo;
        list.add(control);
    } else if (partType == "LH696") {
        //无线PGM
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessPGM";
        control.data = new Map();
        control.data.identifyNum = 255;
        control.data.actionType = 1;
        control.data.pgmID = input.data.barcode;
        list.add(control);
    } else if (partType == "LH791" || partType == "LH792") {
        //无线遥控器
        var control = new Map();
        control.type = "control";
        control.command = "setTelecontrollerInfo";
        control.data = new Map();
        control.data.identifyNum = 255;
        control.data.actionType = 1;
        control.data.barcode = input.data.barcode;
        control.data.partNum = input.data.partitionNo;
        list.add(control);
    } else {
        //仅无线探测器
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessDetectorInfo";
        control.data = new Map();
        control.data.defenceNum = 255;
        control.data.actionType = 1;
        control.data.barcode = input.data.barcode;
        control.data.partNum = input.data.partitionNo;
        list.add(control);
    }


    return list;
}
