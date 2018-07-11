var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partAutoBind
function execute(input, logger) {
    var list = new List();
    var control = new Map();
    list.add(control);
    control.type = "control";
    control.data = new Map();
    control.workMode = 3;
    if (input.data.protectNo == 107) {
        control.pgmNum = 0;
        control.command = "setWiredPGM";
    }
    if (input.data.protectNo >= 108) {
        control.pgmNum = input.data.protectNo - 108;
        control.command = "setWirelessPGM";
    }
    control.time1Flag = 0;
    control.time2Flag = 0;
    control.triggerSrc = input.data.partNo;
    return list;
}