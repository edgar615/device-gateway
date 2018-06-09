var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceControl消息
function execute(input, logger) {

    var list = new List();
    if (input.data.language != undefined) {
        //todo 向上层请求文件
    }

    //布撤防
    defend(input, logger, list);

    return list;
}

function defend(input, logger, list) {
    if (input.data.defend != undefined) {
        var defend = input.data.defendState;
        var areaNum = 8;
        var action = 0;
        if (input.data.partitionNo != undefined) {
            areaNum = input.data.partitionNo;
        }
        if (defend == 1) {
            action = 2;
        } else if (defend == 2) {
            action = 3;
        } else if (defend = 3) {
            action = 1;
        }
        if (action == 0) {
            logger.error("undefined defendState:" + defend);
        }
        var control = new Map();
        control.type = "control";
        control.command = "defenseAction";
        control.data = new Map();
        event.data.areaNum = areaNum;
        event.data.action = action;
        list.add(control);
    }
}