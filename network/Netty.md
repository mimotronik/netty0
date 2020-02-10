# Netty

##原生IO存在的问题
- NIO 的类库和 API 繁杂，使用麻烦：需要熟练掌握 Selector、ServerSocketChannel、SocketChannel、ByteBuffer 等
- 需要具备其他的额外技能：要熟悉 Java 多线程编程，因为 NIO 编程涉及到 Reactor 模式，你必须对多线程和网络编程非常熟悉，才能编写出高质量的 NIO 程序
- 开发工作量和难度都非常大：例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流的处理等等
- JDK NIO 的 Bug：例如臭名昭著的 Epoll Bug，它会导致 Selector 空轮询，最终导致 CPU 100%。直到 JDK 1.7 版本该问题仍旧存在，没有被根本解决。

##Netty简介
- Netty is an asynchronous event-driven network application framework
  for rapid development of maintainable high performance protocol servers & clients.
![netty.jpg](pic/netty/netty.jpg)
- core 说明
    - 零拷贝
    - 通用交互API
    - 可扩展的事件模型
    
##Netty高性能的架构设计
**线程模型介绍**
- 目前存在的线程模型有：
    - 传统阻塞 I/O 服务模型 
    - Reactor模式
- 根据 Reactor 的数量和处理资源池线程的数量不同，有 3 种典型的实现
    - 单 Reactor 单线程
    - 单 Reactor 多线程
    - 主从 Reactor 多线程 
- Netty 线程模式(Netty 主要基于主从 Reactor 多线程模型做了一定的改进，其中主从 Reactor 多线程模型有多个 Reactor)

- [网络模型](网络模型.md)


**单Reactor单线程**
- 方案说明
1. Select 是前面 I/O 复用模型介绍的标准网络编程 API，可以实现应用程序通过一个阻塞对象监听多路连接请求
2. Reactor 对象通过 Select 监控客户端请求事件，收到事件后通过 Dispatch 进行分发
3. 如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接完成后的后续业务处理
4. 如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应
4. Handler 会完成 Read→业务处理→Send 的完整业务流程

- 方案优缺点分析：
    - 优点：模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成
    - 缺点：性能问题，只有一个线程，无法完全发挥多核 CPU 的性能。Handler 在处理某个连接上的业务时，整个进程无法处理其他连接事件，很容易导致性能瓶颈
    - 缺点：可靠性问题，线程意外终止，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障
    - 使用场景：客户端的数量有限，业务处理非常快速，比如 Redis在业务处理的时间复杂度 O(1) 的情况


