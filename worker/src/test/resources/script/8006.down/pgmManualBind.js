var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partManualBind
function execute(input, logger) {
    var list = new List();
    var control = new Map();
    list.add(control);
    control.type = "control";
    control.data = new Map();
    control.workMode = 2;
    if (input.data.protectNo == 107) {
        control.command = "setWiredPGM";
        control.pgmNum = 0;
    }
    if (input.data.protectNo >= 108) {
        control.pgmNum = input.data.protectNo - 108;
        control.command = "setWirelessPGM";
    }
    return list;
}