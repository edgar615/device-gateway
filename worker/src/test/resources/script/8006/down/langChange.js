var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down langChange消息
function execute(input, logger) {

    var list = new List();

    if (input.data.files.length == 0) {
        return list;
    }
    var upgradeFiles = new List();
    for (var i = 0; i < input.data.files.length; i++) {
        var file = input.data.files[i];
        var upgradeFile = new Map();
        upgradeFiles.add(upgradeFile);
        if (file.name == "firmware") {
            upgradeFile.fileType = 1;
        }
        if (file.name == "keyboard") {
            upgradeFile.fileType = 2;
        }
        if (file.name == "voice") {
            upgradeFile.fileType = 3;
        }
        if (file.name == "wireless") {
            upgradeFile.fileType = 4;
        }
        if (file.name == "voiceFirmware") {
            upgradeFile.fileType = 5;
        }
        upgradeFile.filePath = file.filePath;
        upgradeFile.fileLen = file.fileSize;
        upgradeFile.fileMd5 = file.fileMd5;
    }

    var control = new Map();
    control.type = "control";
    control.command = "updateLanguage";
    control.data = new Map();
    control.data.fileCnt = input.data.files.length;
    control.data.languageNum = input.data.language;
    control.data.scheme = input.data.scheme;
    control.data.domainName = input.data.host;
    control.data.port = input.data.port;
    control.data.files = upgradeFiles;
    list.add(control);
    return list;
}