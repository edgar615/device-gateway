公司的一个平台，需要和硬件设备进行交互：

平台 <====> 消息  <====> 设备接入网关

现在在线上跑的项目是在代码中硬编码实现的解析和转换，消息种类特别多，每次协议变动或者有新设备接入都需要修改很多代码，并且重新打包部署。
因此考虑使用Java的ScriptEngine用Nashorn脚本来实现转换，有负责设备接入的通知直接编辑脚本来实现协议的转换功能.

> Nashorn是JVM上的ECMAScript 5.1规范的运行时实现。

根据消息的发送方向，我将消息划分为下列几类：

- down 平台发到协议网关
- control 协议网关发到接入网关
- up 接入网关发到协议网关
- report 协议网关发到平台
- keepalive 设备心跳，虽然也是从接入网关发到协议网关，但是单独列为一类处理
- event 设备事件，虽然也是从接入网关发到协议网关，但是单独列为一类处理
- inner 协议网关内部消息，调用方不应该使用这个类型的消息

inner消息的command有下面几种

- keepalive 心跳 会交由心跳模块处理
- deviceAdded 新增设备，会在redis中存放密钥供接入网关使用（不是很好的设计）
- deviceDeleted 删除设备，会将redis中的密钥删除

keepalive消息的command有下面几种

- keepalive 心跳 会转换为inner类型的keepalive，调用方不需要关心
- connect 建立连接 调用方可以处理一下业务逻辑
- disConnect 断开连接 调用方可以处理一下业务逻辑

event消息的command有下面几种

- new 新事件
- updateImage 更新图片，用于设备互联的图片抓拍
- updateVideo 更新视频，用户设备互联的视频抓拍

down消息的command有下面几种

- deviceAdded 新增设备，会转换为inner类型的deviceAdded，调用方不需要关心
- deviceDeleted 删除设备，会转换为inner类型的deviceDeleted，调用方不需要关心
- deviceControl 控制设备，调用方需要根据属性转换为对应的control消息
- partAdded 新增配件，会转换为inner类型的partAdded，调用方不需要关心
- partDeleted 删除配件，会转换为inner类型的partDeleted，调用方不需要关心
- part.control 控制配件，调用方需要根据属性转换为对应的control消息
- versionNotify 新版本通知，调用方可以自行选择是否将消息发送给设备(数组)
- versionUpgrade 开始升级，调用方可以自行选择是否将消息发送给设备

消息格式
versionNotify:

scheme: 当前固定为HTTP
host: 域名或IP
port: 端口
files: 文件的JSON数组，格式如下
vesionType: 字符串，说明是那种类型的升级文件,与versionReport中定义的版本名称相同
filePath: 文件下载路径
fileLength: 文件大小
fileMd5: 文件的MD5

report消息的command有下面几种

- deviceReport 设备属性变化，调用方需要修改存储设备属性快照
- partDeleted 删除配件的回应，调用方需要删除存储的配件
- partAdded 添加配件的回应，调用方需要添加存储的配件
- partReport 配件属性变化，调用方需要修改存储设备属性快照(数组)
- versionReport 上报版本信息，里面带一个notifyNewVersion的bool属性，设备服务检测到notifyNewVersion=true后，将触发versionNotify事件

up消息的command根据协议文档来操作



**下面的文档已过期**

设备服务向网关发送的消息

- 新增设备
- 删除设备
- 修改设备属性
- 控制设备（拍照）
- 升级
- 添加子设备
- 删除子设备
- 修改子设备属性
- 请求快照

设备向网关发送的消息

- 心跳
- 回复属性修改
- 回复控制命令
- 上报事件
- 回复升级
- 回复添加子设备
- 回复删除子设备
- 回复快照


设备服务向网关发送的消息

主题：v1.event.device.down

deviceAdded 平台新增设备 平台内置，不通过脚本配置
收到新增设备的命令后，在cache中维护设备信息，不需要与设备交互

- 用户
- 加密方式 1-随机，2-约定
- 秘钥
- 通道
- IP
- 在线状态

deviceDeleted 平台删除设备 平台内置，不通过脚本配置
收到删除设备的命令后，在cache中删除设备信息，不需要需设备交互

field.update 平台修改设备属性
需要与设备交互

ControlDevice 平台向设备下发控制指令

- 协议名
- 参数

UpgradeDevice 平台向设备下发升级命令

网关向设备服务发送的设备消息
主题 v1.event.device.up

DeviceOnline 设备上线

DeviceOffline 设备离线

UpdateReplied  设备响应平台修改之后的回应

ControlReplied 设备响应平台控制指令的回应

UpgradeReplied 设备响应平台升级指令的回应

网关向设备服务发送的时间
主题 v1.event.device.report

EventReported 设备向平台发送的事件