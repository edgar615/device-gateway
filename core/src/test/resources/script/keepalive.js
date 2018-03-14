var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

/**
 * 判断是否执行这个脚本
 * @param input
 */
function shouldExecute(input) {
    return true;
}

/**
 * 心跳包
 * @param input
 */
function heartbeat(input) {
    var map = new Map();
    print(input);
    var channel = input.head.from;
    map.channel = channel;
    var clientIp = input.data.content.address;
    map.ip = clientIp;
    return map;
}

/**
 * 控制主设备（网关）
 * 返回的数据中存放的是协议的data属性，head属性根据配置自动填充，data的格式为
 * <pre>
 *
 * </pre>
 * @param input
 * @return 返回协议的列表
 */
function controlPrimaryDevice(input) {
    var list = new List();
    defendState(input, list);
    alarmTime(input, list);
    return list;
}


/**
 * 主设备（网关）属性变化
 */
function primaryDeviceChanged(input) {
    var outDelay = input.data.content.outDelay;
    var inDelay = input.data.content.inDelay;
    var alarmTime = input.data.content.alarmTime;
    var inDelayTime = input.data.content.inDelayTime;
    var outDelayTime = input.data.content.outDelayTime;
    //更新设备状态
    var deviceState = new Map();
    if (alarmTime != -1) {
        deviceState.sirenDuration = alarmTime;
    }
    if (inDelayTime != -1) {
        deviceState.alarmDelay = inDelayTime;
    }
    if (outDelayTime != -1) {
        deviceState.dependDelay = outDelayTime;
    }
    deviceState.put("delayEnable", !delayDisable);
    return deviceState;
}

/**
 * 请求设备快照
 */
function requestSnapshot(input) {

}

/**
 * 上报设备快照
 */
function reportSnapshot(input) {

}

/**
 * 请求升级
 */
function requestUpgrade(input) {

}

/**
 * 上报升级
 */
function reportUpgrade(input) {

}

/**
 * 上报的事件
 */
function reportEvent(input) {

}

//-----------------华丽的分割线--------------------
//下面的函数不是接口定义的函数，是一些私有函数
function defendState(input, result) {
    var defendState = input.data.content.defendState;
    if (defendState == null) {
        return null;
    }
    //布撤防
    if (defendState.equals(1)) {
        var map = new Map();
        map.content = new Map();
        map.resource = "niot";
        map.operation = "set";
        map.content.cmd = "AwayDefend";
        result.add(map);
    } else if (defendState.equals(2)) {
        var map = new Map();
        map.content = new Map();
        map.resource = "niot";
        map.operation = "set";
        map.content.cmd = "HomeDefend";
        result.add(map);
    } else if (defendState.equals(3)) {
        var map = new Map();
        map.content = new Map();
        map.resource = "niot";
        map.operation = "set";
        map.content.cmd = "UnDefend";
        result.add(map);
    }
}

function alarmTime(input, result) {
    //setAlarmTime
    //sirenDuration警笛时长
    var needSetAlarmTime = false;
    //报警时长
    var alarmTime = -1;
    //报警延时
    var inDelayTime = -1;
    //布防延时
    var outDelayTime = -1;

    //-1表示不对设备做修改
    var newSirenDuration = input.data.content.sirenDuration;

    if (newSirenDuration != null) {
        needSetAlarmTime = true;
        alarmTime = newSirenDuration;
    }

    var newDelayEnable = input.data.content.delayEnable;
    var newAlarmDelay = input.data.content.alarmDelay;
    var newDependDelay = input.data.content.dependDelay;

    //如果关闭延时，直接将延时设为0
    //enable变化，判断两个延时值
    if (newDelayEnable != null && !newDelayEnable) {
        needSetAlarmTime = true;
        //关闭延时
        //报警延时
        inDelayTime = 0;
        //布防延时
        outDelayTime = 0;
    } else {
        if (newAlarmDelay != null) {
            needSetAlarmTime = true;
            inDelayTime = newAlarmDelay;
        } else {
            inDelayTime = -1;
        }
        if (newDependDelay != null) {
            needSetAlarmTime = true;
            outDelayTime = newDependDelay;
        } else {
            outDelayTime = -1;
        }
        if (needSetAlarmTime) {
            var map = new Map();
            map.content = new Map();
            map.resource = "niot";
            map.operation = "set";
            map.content.cmd = "SetAlarmTime";
            map.content.alarmTime = alarmTime;
            map.content.inDelayTime = inDelayTime;
            map.content.outDelayTime = outDelayTime;
            result.add(map);
        }
    }

}