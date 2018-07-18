var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down versionUpgrade消息
function execute(input, logger) {

    var list = new List();

    var control = new Map();
    control.type = "control";
    control.command = "upgrade";
    control.data = new Map();
    control.data.type = 1;
    list.add(control);
    return list;
}