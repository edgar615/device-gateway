var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//up syncDeviceInfoAck
function execute(input, logger) {

    var result = input.data.result;
    if (result == 0) {
        logger.info("syncDeviceInfoAck succeeded");
    } else {
        logger.error("syncDeviceInfoAck failed");
    }
    return new List();
}