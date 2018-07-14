var Map = Java.type("java.util.HashMap");
var List = Java.type("java.util.ArrayList");

//down deviceMute
function execute(input, logger) {

    var list = new List();

    var control = new Map();
    control.type = "control";
    control.command = "closeVoice";
    control.data = new Map();
    //1关闭主机内置警号, 2关闭主机外接有线警号,3关闭主机所有无线警号,4关闭主机所有警号
    control.data.type = 4;
    list.add(control);
    return list;
}
