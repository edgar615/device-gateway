var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

/**
 * 判断是否执行这个脚本
 * @param input
 */
function shouldExecute(input) {
    return "connect" == input.type;
}

/**
 * 执行脚本
 * @param input
 * @returns {*}
 */
function execute(input) {
    stderr.accept("aaaaaaa");
    logger.error(input.traceId, "test:{} {}", "foo", "bar");
    var inquiryF1Version = new Map();
    inquiryF1Version.type = "control";
    inquiryF1Version.command = "inquiryF1Version";
    inquiryF1Version.data = new Map();

    var list = new List();
    list.add(inquiryF1Version);
    return list;
}