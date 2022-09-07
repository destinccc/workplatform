1、rabbitmq消息队列功能说明
rabbitmq消息主要用于工作台页面操作用户、组织、项目等操作时给cmdb项目发送同步消息数据，采用的是direct模式，项目启动时会根据配置的exchangeName、
routingKey、queueName生成exchangeName以及通过routingKey关联的queueName队列。首次启动时需要先启动工作台服务创建exchange、queue，再启动cmdb服务，否则可能出现找不到exchange的情况。

2、redis消息队列功能说明
本服务采用了redisStream的消息功能。

3、页面配置说明
初始化sql脚本执行后，有部分url地址需要根据实际部署环境进行修改，主要包括以下配置：
(1)监控大屏地址
监控大屏的访问地址需要在系统管理-》菜单管理中的监控大屏，点击修改，根据实际部署环境修改路由地址选项，例如uat环境为：http://10.229.1.142/gdmercury/monitor/bigScreen。
(2)模块管理配置
统一门户、cmdb、monitor、workOrder等模块需要配置base路径，一般只需要修改ip地址即可。
(3)用户初始化密码配置
进入系统管理-》参数设置，里面的用户管理-账号初始密码可修改用户的初始化密码
(4)其它配置说明
进入系统管理-》参数设置，可针对用户账号、密码正则匹配进行设置，还包括大屏的标题、系统名称、监控大屏的地理位置进行配置