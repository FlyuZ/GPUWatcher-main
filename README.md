# GPUWatcher
用于监控内网服务器GPU使用情况
 
使用frp做转发，GPU服务器使用python获取GPU使用信息，通过tcp（or http）发送到服务器

### FRP安全问题
1）使用强Token
2）设置不常用端口号
3）使用自己熟悉的服务器做转发
