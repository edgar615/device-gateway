var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceControl
function execute(input, logger) {

    var list = new List();
    if (input.data.defendState != undefined) {
        var defend = input.data.defendState;
        var action = 0;
        if (defend == 1) {
            action = 2;
        } else if (defend == 2) {
            action = 3;
        } else if (defend = 3) {
            action = 1;
        }
        if (action == 0) {
            logger.error("undefined defendState:" + defend);
        } else {
            var control = new Map();
            control.type = "control";
            control.command = "defenceAction";
            control.data = new Map();
            control.data.areaNum = 8;//所有分区
            control.data.action = action;
            list.add(control);
        }
    }

    return list;
}