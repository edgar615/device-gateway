var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up defenseActionAck消息
function execute(input, logger) {

    var result = input.data.result;
    if (result != 0) {
        logger.error("defenseAction failed:" + result);
        return;
    }
    logger.info("defenseAction succeeded");
}