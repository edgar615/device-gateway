var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");
var Integer = Java.type("java.lang.Integer");

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
    if (input.data.protectNo >= 107 && input.data.protectNo <= 112) {
        var control = new Map();
        control.type = "control";
        control.data = new Map();
        if (input.data.protectNo == 107) {
            control.command = "setWiredPGM";
            control.data.actionType = 0;
        } else {
            control.command = "setWirelessPGM";
            control.data.actionType = 0;
            control.data.identifyNum = new Integer(input.data.protectNo - 108);
            control.data.pgmID = input.data.barcode;
        }
        var flag = false;
        if (input.data.workMode != undefined) {
            control.data.workMode = input.data.workMode;
            flag = true;
        }
        if (input.data.triggerPart != undefined && input.data.triggerPart >=0 && input.data.triggerPart <= 97) {
            control.data.triggerSrc = input.data.triggerPart;
            flag = true;
        }
        if (input.data.triggerCondition != undefined && input.data.triggerCondition >=1 && input.data.triggerCondition <= 3) {
            control.data.triggerSrc = new Integer(input.data.triggerCondition + 97);
            flag = true;
        }
        control.data.time1Flag = 0;
        if (input.data.pgmTimer1 != undefined) {
            control.data.time1Flag = 1;
            control.data.time1Enable = input.data.pgmTimer1 ? 1 : 0;
            flag = true;
        }
        if (input.data.openHour1 != undefined) {
            control.data.time1Flag = 1;
            control.data.openHour1 = input.data.openHour1;
            flag = true;
        }
        if (input.data.openMin1 != undefined) {
            control.data.time1Flag = 1;
            control.data.openMinute1 = input.data.openMin1;
            flag = true;
        }
        if (input.data.closeHour1 != undefined) {
            control.data.time1Flag = 1;
            control.data.closeHour1 = input.data.closeHour1;
            flag = true;
        }
        if (input.data.closeMin1 != undefined) {
            control.data.time1Flag = 1;
            control.data.closeMinute1 = input.data.closeMin1;
            flag = true;
        }
        if (input.data.week1 != undefined) {
            control.data.time1Flag = 1;
            control.data.week1 = input.data.week1;
            flag = true;
        }
        control.data.time2Flag = 0;
        if (input.data.pgmTimer2 != undefined) {
            control.data.time2Flag = 1;
            control.data.time2Enable = input.data.pgmTimer2 ? 1 : 0;
            flag = true;
        }
        if (input.data.openHour2 != undefined) {
            control.data.time2Flag = 1;
            control.data.openHour2 = input.data.openHour2;
            flag = true;
        }
        if (input.data.openMin2 != undefined) {
            control.data.time2Flag = 1;
            control.data.openMinute2 = input.data.openMin2;
            flag = true;
        }
        if (input.data.closeHour2 != undefined) {
            control.data.time2Flag = 1;
            control.data.closeHour2 = input.data.closeHour2;
            flag = true;
        }
        if (input.data.closeMin2 != undefined) {
            control.data.time2Flag = 1;
            control.data.closeMinute2 = input.data.closeMin2;
            flag = true;
        }
        if (input.data.week2 != undefined) {
            control.data.time2Flag = 1;
            control.data.week2 = input.data.week2;
            flag = true;
        }
        if (input.data.workTime != undefined) {
            control.data.workTime = input.data.workTime;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }

    return list;
}
