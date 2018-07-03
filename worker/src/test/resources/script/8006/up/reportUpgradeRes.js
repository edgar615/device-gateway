var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up reportUpgradeRes消息
function execute(input, logger) {

    var list = new List();

    var type = input.data.type;
    var upgradeType = 'unkown';
    if (type == 1) {
        upgradeType = "firmware";
    } else if (type == 2) {
        upgradeType = "language";
        //报告语言
        var language = input.data.result;
        var report = new Map();
        report.type = "report";
        report.command = "deviceReport";
        report.data = new Map();
        event.data.language = input.data.language;
        list.add(report);
    } else {
        logger.error('undefined type: ' + type)
    }
    var result = input.data.result;
    if (result == 1 || result == 2) {
        logger.info(upgradeType + " upgrade succeeded");
    } else {
        logger.error(upgradeType + " upgrade failed");
    }
    return list;
}