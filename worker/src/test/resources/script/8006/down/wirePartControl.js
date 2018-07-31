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
    if (input.data.protectNo >= 64 && input.data.protectNo <= 97) {
        var control = new Map();
        control.type = "control";
        control.command = "setWireDetectorInfo";
        control.data = new Map();
        var flag = false;
        control.data.defenceNum = input.data.protectNo;
        if (input.data.partitionNo != undefined) {
            control.data.partNum = input.data.partitionNo;
            flag = true;
        }
        if (input.data.masterUnDefend != undefined) {
            control.data.removaValid = input.data.masterUnDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.masterAwayDefend != undefined) {
            control.data.awayValid = input.data.masterAwayDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.masterHomeDefend != undefined) {
            control.data.atHomeValid = input.data.masterHomeDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.unicodeName != undefined) {
            var nameList = new List();
            var nameArray = input.data.unicodeName.split("\\u").slice(1);
            for (var j = 0; j < nameArray.length; j ++) {
                nameList.add(nameArray[j]);
            }
            control.data.name = nameList;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }

    return list;
}
