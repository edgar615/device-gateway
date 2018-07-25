var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down partControl
function execute(input, logger) {

    /**
     * 64个无线探测器，34个有线探测器，32个无线遥控器，7个无线警号，1个内置警号，1个有线外接警号，5个无线pgm，1个有线外接pgm，32个rfid
     * 为了方便平台计算，没有按照设备的编号，而是转换为统一的变化
     * 无线、有线探测器由设备统一：0~63无线防区 64~65系统自带的两个有线防区 66~97扩展的32个有线防区
     *  98内置警号 99外接有线警号 100~106 无线警号
     *  107 有线PGM 108~112 无线PGM
     *  113~144 无线遥控器
     *  145~176 RFID
     */
    var list = new List();
    if (input.data.protectNo >= 100 && input.data.protectNo <= 106) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessAlarmInfo";
        control.data = new Map();
        control.data.actionType = 0;
        control.data.barcode = input.data.barcode;
        control.data.defenceNum = input.data.protectNo - 100;
        var flag = false;
        if (input.data.partitionNo != undefined) {
            control.data.partNum = input.data.partitionNo;
            flag = true;
        }
        if (input.data.sirenSwitch != undefined) {
            control.data.enable = input.data.sirenSwitch;
            flag = true;
        }
        if (input.data.lightSwitch != undefined) {
            control.data.alarmLamp = input.data.lightSwitch;
            flag = true;
        }
        if (input.data.sirenVolume != undefined) {
            control.data.alarmVolume = input.data.sirenVolume;
            flag = true;
        }
        if (input.data.sirenDuration != undefined) {
            control.data.alarmTime = input.data.sirenDuration;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }

    return list;
}

