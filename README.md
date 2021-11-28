# NettyWebFrame
基于Netty编写的Http容器框架

利用Netty的NIO网络通讯机制实现的一个支持高并发更好性能的web框架。
偏向于http报文底层的解析，功能类似于spring的简单实现。
支持基本的静态资源传输、GET/POST报文解析、json报文序列化转换、cookie检测、session创建、http心跳机制。

内有一个简单的测试实例TestRun.java，测试了基本的post、get报文传输、java对象序列化以及静态资源传输。

api有空再写...
