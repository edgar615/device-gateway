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
    var list = new List();
    return list;
}