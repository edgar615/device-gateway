var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down rfidControl
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
    if (input.data.protectNo >= 145 && input.data.protectNo <= 176) {
        var control = new Map();
        control.type = "control";
        control.command = "setRFIDInfo";
        control.data = new Map();
        list.add(control);
        //0内置 1外接
        control.identifyNum = input.data.protectNo - 145;

        if (input.data.controlPartition != undefined) {
            var partition = [1,2,4,8,16,32,64,128];
            var bitPartition = 0;
            control.ctrlType = 0;
            for (var i = 0; i < input.data.controlPartition.length; i ++) {
                bitPartition += partition[input.data.controlPartition[i] - 1]
            }
            control.partNum = bitPartition;
            flag = true;
        }
        if (input.data.controlPgm != undefined) {
            var pgm = [1,2,3,8,16,32];
            var bitPgm = 0;
            control.ctrlType = 1;
            for (var i = 0; i < input.data.controlPgm.length; i ++) {
                var pgmNo = input.data.controlPgm[i];
                bitPgm += pgm[pgmNo - 107]
            }
            control.PGM = pgm;
            flag = true;
        }
        if (flag) {
            list.add(control);
        }
    }

    return list;
}

