var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setUpgradeInfoAck
function execute(input, logger) {

    var result = input.data.result;
    if (result == 1) {
        logger.error("setUpgradeInfo failed");
        return new List();
    }
    if (result == 2) {
        logger.error("setUpgradeInfo failed: host error");
        return new List();
    }
    if (result == 3) {
        logger.error("setUpgradeInfo failed: port error");
        return new List();
    }
    if (result == 4) {
        logger.error("setUpgradeInfo failed: fileSize error");
        return new List();
    }
    if (result == 5) {
        logger.error("setUpgradeInfo failed: filePath error");
        return new List();
    }
    if (result != 0) {
        logger.error("setUpgradeInfo failed: unknown result");
        return new List();
    }
    logger.info("setUpgradeInfo succeeded");
    return new List();
}