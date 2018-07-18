var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down langChange消息
function execute(input, logger) {

    var list = new List();

    if (files.length == 0) {
        return list;
    }
    var upgradeFiles = new List();
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        var upgradeFile = new Map();
        upgradeFiles.add(upgradeFile);
        upgradeFile.fileType = file.versionType;
        upgradeFile.filePath = file.filePath;
        upgradeFile.fileLen = file.fileSize;
        upgradeFile.fileMd5 = file.fileMd5;
    }

    var control = new Map();
    control.type = "control";
    control.command = "updateLanguage";
    control.data = new Map();
    control.data.fileCnt = files.length;
    control.data.languageNum = input.data.language;
    control.data.scheme = input.data.scheme;
    control.data.domainName = input.data.host;
    control.data.port = input.data.port;
    control.data.files = upgradeFiles;
    list.add(control);
    return list;
}