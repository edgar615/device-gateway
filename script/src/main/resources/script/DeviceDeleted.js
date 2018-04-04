var Map = Java.type("java.util.HashMap");

/**
 * 判断是否执行这个脚本
 * @param input
 */
function shouldExecute(input) {
    return (input.head.direction == "down")
        && (input.data.resource = "DeviceDeleted");
}

/**
 * 删除设备
 * @param input
 */
function DeviceDeleted(input) {
    var map = new Map();
    map.id = input.data.content.id;
    return map;
}