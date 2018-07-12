var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down pgmControl
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
    if (input.data.protectNo >= 107 && input.data.protectNo <= 112) {
        var control = new Map();
        control.type = "control";
        if (input.data.protectNo == 107) {
            control.command = "setWirelessPGM";
        } else {
            control.command = "setWiredPGM";
        }
        control.data = new Map();
        var flag = false;
        control.defenceNum = input.data.protectNo - 107;
        if (input.data.workMode != undefined) {
            control.workMode = workMode;
            flag = true;
        }
        if (input.data.autoPartId != undefined) {
            control.triggerSrc = autoPartId;
            flag = true;
        }
        control.time1Flag = 0;
        if (input.data.pgmTimer1 != undefined) {
            control.time1Flag = 1;
            control.time1Enable = input.data.pgmTimer1 ? 1 : 0;
            flag = true;
        }
        if (input.data.openHour1 != undefined) {
            control.time1Flag = 1;
            control.openHour1 = input.data.openHour1;
            flag = true;
        }
        if (input.data.openMinute1 != undefined) {
            control.time1Flag = 1;
            control.openMinute1 = input.data.openMinute1;
            flag = true;
        }
        if (input.data.closeHour1 != undefined) {
            control.time1Flag = 1;
            control.closeHour1 = input.data.closeHour1;
            flag = true;
        }
        if (input.data.closeMinute1 != undefined) {
            control.time1Flag = 1;
            control.closeMinute1 = input.data.closeMinute1;
            flag = true;
        }
        if (input.data.week1 != undefined) {
            control.time1Flag = 1;
            control.week1 = input.data.week1;
            flag = true;
        }
        control.time2Flag = 0;
        if (input.data.pgmTimer2 != undefined) {
            control.time2Flag = 1;
            control.time2Enable = input.data.pgmTimer2 ? 1 : 0;
            flag = true;
        }
        if (input.data.openHour2 != undefined) {
            control.time2Flag = 1;
            control.openHour2 = input.data.openHour2;
            flag = true;
        }
        if (input.data.openMinute2 != undefined) {
            control.time2Flag = 1;
            control.openMinute2 = input.data.openMinute2;
            flag = true;
        }
        if (input.data.closeHour2 != undefined) {
            control.time2Flag = 1;
            control.closeHour2 = input.data.closeHour2;
            flag = true;
        }
        if (input.data.closeMinute2 != undefined) {
            control.time2Flag = 1;
            control.closeMinute2 = input.data.closeMinute2;
            flag = true;
        }
        if (input.data.week2 != undefined) {
            control.time2Flag = 1;
            control.week2 = input.data.week2;
            flag = true;
        }
        if (input.data.workTime != undefined) {
            control.workTime = input.data.workTime;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }

    return list;
}
