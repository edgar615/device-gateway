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
        upgradeFile.fileType = file.name;
        upgradeFile.filePath  = file.filePath;
        upgradeFile.fileLen  = file.fileLength;
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