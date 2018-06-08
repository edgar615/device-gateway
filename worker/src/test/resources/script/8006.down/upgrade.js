var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

function execute(input, logger) {

    var list = new List();

    //语言
    var control = new Map();
    control.type = "control";
    control.command = "device.upgrade";
    control.data = new Map();
    event.data.type = 1;
    list.add(control);
    return list;
}