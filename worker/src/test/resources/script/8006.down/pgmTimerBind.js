var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partTimerBind
function execute(input, logger) {
    var list = new List();
    var control = new Map();
    list.add(control);
    control.type = "control";
    control.data = new Map();
    control.workMode = 1;
    if (input.data.protectNo == 107) {
        control.command = "setWiredPGM";
        control.pgmNum = 0;
    }
    if (input.data.protectNo >= 108) {
        control.pgmNum = input.data.protectNo - 108;
        control.command = "setWirelessPGM";
    }
    if (input.data.pgmTimer1 != undefined) {
        control.time1Flag = input.data.pgmTimer1;
        control.openHour1  = input.data.openHour1;
        control.openMinute1  = input.data.openMinute1;
        control.closeHour1  = input.data.closeHour1;
        control.closeMinute1  = input.data.closeMinute1;
        control.week1  = input.data.pgmTimer1;

    }

    return list;
}