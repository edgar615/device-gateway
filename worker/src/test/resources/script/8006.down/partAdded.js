var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partAdded
function execute(input, logger) {

    var list = new List();
    var control = new Map();
    control.type = "control";
    control.command = "setWirelessDetectorInfo";
    control.data = new Map();
    event.data.defenceNum = input.data.protectNo;
    event.data.actionType = 1;
    event.data.barcode = input.data.barcode;
    event.data.partNum = input.data.partitionNo;
    list.add(control);

    return list;
}
