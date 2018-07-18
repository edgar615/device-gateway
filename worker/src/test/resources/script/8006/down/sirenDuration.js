var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceControl,sirenDuration的是内置警号的时长
function execute(input, logger) {

    var control = new Map();
    control.type = "control";
    control.command = "setWiredAlarmInfo";
    control.data = new Map();
    var flag = false;
    control.data.defenceNum = 98;
    if (input.data.sirenDuration != undefined) {
        control.data.alarmTime = input.data.sirenDuration;
        flag = true;
    }
    var list = new List();
    if (flag) {
        list.add(control);
    }
    return list;
}