var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setUpgradeInfoAck消息
function execute(input, logger) {

    var result = input.data.result;
    if (result == 0) {
        logger.info( "setUpgradeInfo succeeded");
    } else {
        logger.error("setUpgradeInfo failed");
    }
    return new List();
}