# NettyWebFrame
基于Netty编写的Http容器框架

利用Netty的NIO网络通讯机制实现的一个支持高并发更好性能的web框架。  
偏向于http报文底层的解析，功能类似于spring的简单实现。  
支持基本的静态资源传输、GET/POST报文解析、json报文序列化转换、cookie检测、session创建、http心跳机制、websocket。  
内有一个简单的测试实例TestRun.java，测试了基本的post、get报文传输、java对象序列化以及静态资源传输等。  

v1.0 2021/11/26  
完成基本的GET/POST处理功能，目前只有一个json报文的序列化解析工具，但是提供了一个接口供用户去自定义序列化工具。  
提供一个抽象业务处理类RequestAction，用户可以通过继承该类并绑定url实现事务的处理。  
所有的业务绑定后由业务工厂RequestActionFactory自动生成匹配的键值对。  

v1.1 2021/11/27  
实现本地静态资源的路径配置，完成了适用于任何文件类型传输的报文构造器。  
v1.2 2021/11/27  
将报文集合封装为一个数据结构ResponsePackage,为处理不止一条报文的情况，并且添加了标签机制，表明了报文包的类型(例如：KEEP_ALIVE、STATIC_FILE等)用于ServerHandler处理。  

V2.0 2021/11/28   
添加了session管理模块，可以自定义一个session所对应的一个甚至多个对象的内容，可以通过session对连接的客户端实现分发sessionId以及cookie标签，可以对请求的报文进行sessionId验证。  

V3.0 2021/11/29  
优化了session管理器模块，添加了websocket长链接处理功能。  
修改了ServerHandler的结构以及对业务处理模块进行重新的改动。  
新增了一个测试文件TestWebsocketAction.java，用于展示websocket的效果。  
新增资源文件hello.html，简易的长链接前后端聊天室。  

api有空再写...  
