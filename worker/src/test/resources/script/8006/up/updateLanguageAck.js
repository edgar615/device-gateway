var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up updateLanguageAck
function execute(input, logger) {

    var result = input.data.result;
    //0 成功1升级文件个数错误 2域名错误 3端口错误 4长度错误 5路径错误 6语言相同 7语言类型错误
    if (result == 0) {
        logger.info( "updateLanguageAck succeeded");
    } else if (result == 1) {
        logger.error("updateLanguageAck failed: file num");
    } else if (result == 2) {
        logger.error("updateLanguageAck failed: domain");
    } else if (result == 3) {
        logger.error("updateLanguageAck failed: port");
    } else if (result == 4) {
        logger.error("updateLanguageAck failed: fileLength");
    } else if (result == 5) {
        logger.error("updateLanguageAck failed: filePath");
    } else if (result == 6) {
        logger.error("updateLanguageAck failed: same language");
    } else if (result == 7) {
        logger.error("updateLanguageAck failed: error language");
    }  else {
        logger.info( "updateLanguageAck: type");
    }
    //没有语言信息，通过上报更新
    return new List();

}