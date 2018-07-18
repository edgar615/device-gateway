var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up closeVoiceAck
function execute(input, logger) {

    var result = input.data.result;
    //0x00关闭主机内置警号, 0x01关闭主机外接有线警号,0x02关闭主机所有无线警号,       0x03关闭主机所有警号，0xFF参数错误
    if (result == 0) {
        logger.error("mute succeeded: built-in alarm:");
    } else if (result == 1) {
        logger.info("mute succeeded: external alarm");
    } else if (result == 2) {
        logger.info("mute succeeded: wireless alarm");
    } else if (result == 3) {
        logger.info("mute succeeded: all alarm");
    } else if (result == 255) {
        logger.info("mute failed: invalid args");
    } else {
        logger.error("mute failed: unknown result");
    }
    return new List();
}