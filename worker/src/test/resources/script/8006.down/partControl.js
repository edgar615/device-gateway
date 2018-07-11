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
    wirelessPart(input, logger, list);
    wirePart(input, logger, list);
    wireAlarm(input, logger, list);
    wirelessAlarm(input, logger, list);
    wirelessControl(input, logger, list);

    return list;
}

function wirelessPart(input, logger, list) {
    if (input.data.protectNo >= 0 && input.data.protectNo < 64) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessDetectorInfo";
        control.data = new Map();
        control.actionType = 0;
        control.defenceNum = input.data.protectNo;
        var flag = false;
        if (input.data.partitionNo != undefined) {
            control.partNum = input.data.partitionNo;
            flag = true;
        }
        if (input.data.masterUnDefend != undefined) {
            control.removaValid = input.data.masterUnDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.masterAwayDefend != undefined) {
            control.awayValid = input.data.masterAwayDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.masterHomeDefend != undefined) {
            control.atHomeValid = input.data.masterHomeDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.offlineSwitch != undefined) {
            control.loseDetectSwitch = input.data.offlineSwitch ? 1 : 0;
            flag = true;
        }
        //门磁特有
        if (input.data.welcomeSwitch != undefined) {
            control.usherSwitch = input.data.welcomeSwitch ? 1 : 0;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }
}

function wirePart(input, logger, list) {
    if (input.data.protectNo >= 64 && input.data.protectNo <= 97) {
        var control = new Map();
        control.type = "control";
        control.command = "setWireDetectorInfo";
        control.data = new Map();
        var flag = false;
        control.defenceNum = input.data.protectNo;
        if (input.data.partitionNo != undefined) {
            control.partNum = input.data.partitionNo;
            flag = true;
        }
        if (input.data.masterUnDefend != undefined) {
            control.removaValid = input.data.masterUnDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.masterAwayDefend != undefined) {
            control.awayValid = input.data.masterAwayDefend ? 1 : 0;
            flag = true;
        }
        if (input.data.masterHomeDefend != undefined) {
            control.atHomeValid = input.data.masterHomeDefend ? 1 : 0;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }
}

function wireAlarm(input, logger, list) {
    if (input.data.protectNo >= 98 && input.data.protectNo <= 99) {
        var control = new Map();
        control.type = "control";
        control.command = "setWiredAlarmInfo";
        control.data = new Map();
        list.add(control);
        //0内置 1外接
        control.defenceNum = input.data.protectNo - 98;
        var flag = false;
        if (input.data.sirenSwitch != undefined) {
            control.enable = input.data.sirenSwitch;
            flag = true;
        }
        if (input.data.sirenDuration != undefined) {
            control.alarmTime = input.data.sirenDuration;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }
}

function wirelessAlarm(input, logger, list) {
    if (input.data.protectNo >= 100 && input.data.protectNo <= 106) {
        var control = new Map();
        control.type = "control";
        control.command = "setWirelessAlarmInfo";
        control.data = new Map();
        control.actionType = 0;
        control.defenceNum = input.data.protectNo - 100;
        var flag = false;
        if (input.data.partitionNo != undefined) {
            control.partNum = input.data.partitionNo;
            flag = true;
        }
        if (input.data.sirenSwitch != undefined) {
            control.enable = input.data.sirenSwitch;
            flag = true;
        }
        if (input.data.lightSwitch != undefined) {
            control.alarmLamp = input.data.lightSwitch;
            flag = true;
        }
        if (input.data.sirenVolume != undefined) {
            control.alarmVolume = input.data.sirenVolume;
            flag = true;
        }
        if (input.data.sirenDuration != undefined) {
            control.alarmTime = input.data.sirenDuration;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }
}

function wirelessControl(input, logger, list) {
    if (input.data.protectNo >= 113 && input.data.protectNo <= 144) {
        var control = new Map();
        control.type = "control";
        control.command = "setTelecontrollerInfo";
        control.data = new Map();
        control.actionType = 0;
        control.defenceNum = input.data.protectNo - 113;
        var flag = false;
        if (input.data.partitionNo != undefined) {
            control.partNum = input.data.partitionNo;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }
}
