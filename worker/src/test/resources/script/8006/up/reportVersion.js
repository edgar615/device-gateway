var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

function execute(input, logger) {

    var list = new List();
    var event = new Map();
    event.type = "report";
    event.command = "version.reported";
    event.data = new Map();
    event.data.firmwareVersion = input.data.currentVer;
    event.data.nextFirmwareVersion = input.data.bakVer;
    //keyboardVersion
    event.data.nextKeyboardVersion = input.data.bakKeyVer;
    event.data.voiceVersion = input.data.voiceVer;
    list.add(event);

    //语言
    var report = new Map();
    report.type = "report";
    report.command = "device.reported";
    report.data = new Map();
    event.data.language = input.data.language;
    list.add(report);

    return list;
}