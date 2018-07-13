var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

// up reportVersion
function execute(input, logger) {

    var list = new List();
    var event = new Map();
    event.type = "report";
    event.command = "versionReport";
    event.data = new Map();
    // currentVer=LHD8006-STM32-RF-V0.0.1, bakVer=LHD8006-STM32-RF-V0.0.1, bakKeyVer=DK808-NT100-RF-V0.0.1, voiceVer=VOICE-EN-V0.0.1
    event.data.firmwareVersion = input.data.currentVer.substr("LHD8006-STM32-RF-V".length);
    event.data.nextFirmwareVersion = input.data.bakVer.substr("LHD8006-STM32-RF-V".length);
    event.data.nextKeyboardVersion = input.data.bakKeyVer.substr("DK808-NT100-RF-V".length);
    event.data.voiceVersion = input.data.voiceVer.substr("VOICE-EN-V".length);
    event.data.wirelessVersion = input.data.wirelessVer;
    event.data.voiceFirmwareVersion = input.data.voiceFirmwareVer;
    event.data.notifyNewVersion = true;
    list.add(event);

    //语言
    var report = new Map();
    report.type = "report";
    report.command = "deviceReport";
    report.data = new Map();
    event.data.language = input.data.language;
    list.add(report);

    return list;
}