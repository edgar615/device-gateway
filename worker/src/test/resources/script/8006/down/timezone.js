var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceControl消息
function execute(input, logger) {

    var list = new List();
    if (input.data.timeZone != undefined) {
        var timeZone = input.data.timeZone;
        var control = new Map();
        control.type = "control";
        control.command = "setTimeZone";
        control.data = new Map();
        event.data.timeZone = timeZone + 12;
        list.add(control);
    }

    return list;
}
