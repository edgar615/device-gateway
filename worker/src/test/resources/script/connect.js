var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

/**
 * 执行脚本
 * @param input
 * @returns {*}
 */
function execute(input, logger) {
    //stderr.accept("aaaaaaa");
    logger.error("xxxxxx");
    var inquiryF1Version = new Map();
    inquiryF1Version.type = "control";
    inquiryF1Version.command = "inquiryF1Version";
    inquiryF1Version.data = new Map();

    var list = new List();
    list.add(inquiryF1Version);
    return list;
}