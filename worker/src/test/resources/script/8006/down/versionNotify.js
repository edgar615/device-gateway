var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down versionNotify消息
function execute(input, logger) {

    var list = new List();

    if (input.data.files.length == 0) {
        return list;
    }
    var upgradeFiles = new List();
    for (var i = 0; i < input.data.files.length; i ++) {
        var file = input.data.files[i];
        var upgradeFile = new Map();
        upgradeFiles.add(upgradeFile);
        //升级文件类型,0x01表示主机固件,0x02表示键盘固件,0x03表示语音文件,0x04表示无线模块固件,0x05表示语音模块固件
        if (file.name == "nextFirmwareVersion") {
            upgradeFile.fileType = 1;
        }
        if (file.name == "voiceVersion") {
            upgradeFile.fileType = 3;
        }
        if (file.name == "wirelessVersion") {
            upgradeFile.fileType = 4;
        }
        if (file.name == "voiceFirmwareVersion") {
            upgradeFile.fileType = 5;
        }
        upgradeFile.filePath  = file.filePath + "@" + file.version;
        upgradeFile.fileLen  = file.fileSize;
        upgradeFile.fileMd5  = file.fileMd5;
    }

    var control = new Map();
    control.type = "control";
    control.command = "setUpgradeInfo";
    control.data = new Map();
    control.data.fileCnt = input.data.files.length;
    control.data.scheme  = input.data.scheme;
    control.data.domainName  = input.data.host;
    control.data.port  = input.data.port;
    control.data.files = upgradeFiles;
    list.add(control);
    return list;
}