var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up upgradeAck消息
function execute(input, logger) {

    var result = input.data.result;
    if (result == 0) {
        logger.info("start upgrade");
    } else {
        logger.error("failed start upgrade");
    }
    //todo 更新设备的升级结果
    return new List();
}