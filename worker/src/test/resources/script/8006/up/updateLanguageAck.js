var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up setTimeZoneAck
function execute(input, logger) {

    var result = input.data.result;
    if (result == 0) {
        logger.info( "updateLanguageAck succeeded");
    } else if (result == 1) {
        logger.error("updateLanguageAck failed");
    } else {
        logger.info( "updateLanguageAck: same language");
    }
    return new List();

}