var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partDeleted
function execute(input, logger) {

    var list = new List();
    if (input.data.protectNo >= 0 && input.data.protectNo <= 63) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessDetectorInfo";
        control.data = new Map();
        event.data.defenceNum = input.data.protectNo;
        event.data.actionType = 1;
        event.data.barcode = "0";
        list.add(control);
    }
    return list;
}
