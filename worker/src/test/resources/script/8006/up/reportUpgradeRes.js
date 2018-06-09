var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportUpgradeRes消息
function execute(input, logger) {

    var type = input.data.type;
    var upgradeType = 'unkown';
    if (type == 1) {
        upgradeType = "firmware";
    } else if (type == 2) {
        upgradeType = "language";
    } else {
        logger.error('undefined type: ' + type)
    }
    var result = input.data.result;
    if (result == 0) {
        logger.info(upgradeType + " upgrade succeeded");
    } else {
        logger.error(upgradeType + " upgrade failed");
    }
}