var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partitionControl
function execute(input, logger) {

    var list = new List();
    var control = new Map();
    control.type = "control";
    control.command = "defenceAction";
    control.data = new Map();
    if (input.data.defendState != undefined && input.data.partitionNo != undefined) {
        var defend = input.data.defendState;
        control.data.areaNum = input.data.partitionNo;
        if (defend == 1) {
            control.data.action = 2;
        } else if (defend == 2) {
            control.data.action = 3;
        } else if (defend = 3) {
            control.data.action = 0;
        }
        if (control.data.action == undefined) {
            logger.error("undefined defendState:" + defend);
        } else {
            list.add(control);
        }
    }

    return list;
}
