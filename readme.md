# version2
## version2.part1

Common包负责传输的中间信息 包括服务层的实现和对应信息的定义
Client包负责发送调用信息
Server包负责解读调用信息 然后执行服务层

message包下两个类 定义request和response的http格式

pojo包下定义用户的信息 service层实际传输的数据（在request和respone之上）

  
proxy包负责底层传输协议 ClientProxy定义了代理机制  处理封装、发送请求和接受响应的工作
IoClient则是定义底层传输实际的过程



ServiceProvider  管理服务接口
workthread 实现在多线程环境中接受来自客户端的请求  workthread自己就是一个线程 
在server的两个实现中会新建线程 把workthread放进去

SimpleRPCRPCServer仅仅是有任务就新建线程，没有管理
而ThreadPool版本实现了线程池来管理

最后是TestServer  测试服务端

## part2
netty主要多了channel的概念
引入了netty框架 

Client增加了rpcclient接口(netty相关) 并与上面服务器相似有简单版和netty版
简单版和part1的ioclient一样
重点在netty版的
NettyClientInitializer定义处理的责任链  底层是链表

common包基本没变

Server包与Client包相似 引入netty版的实现

NettyServerInitializer定义责任链 最后调用 NettyRPCServerHandler来处理调用接口

NettyRpcClient(调用clientproxy 负责代理接口)负责发


步骤	调用方	方法	作用
1	NettyRpcClient	bootstrap.connect(host, port).sync();	连接服务器，创建 Channel

2	NettyClientInitializer	initChannel(SocketChannel ch)	初始化 Pipeline

3	NettyRpcClient	channel.writeAndFlush(request);	发送请求到服务器

4	服务器		处理 RpcRequest 并返回 RpcResponse

5	NettyClientHandler	channelRead0(ctx, response)	处理 RpcResponse

6	NettyClientHandler	ctx.channel().attr(key).set(response);	将 RpcResponse 存入 Channel

7	NettyClientHandler	ctx.channel().close();	关闭 Channel，通知 NettyRpcClient

8	NettyRpcClient	channel.attr(key).get();	读取 RpcResponse 并返回

### part3
引入zookeeper来管理连接(多个连接地址和端口)

其他都没特别大的变化 只是新增zookeeper的配置 

注意！！！  要将服务注册到zookeeper里面  根据服务名和接口地址创建节点

# version2
实现自定义编码器解码器 

建立zookeeper本地缓存

## part1
没啥难的 就是文档没有说一些小变化 导致找BUG花了很多时间

定义好了 新的序列化器后 然后定义新的解码器和编码器

编码器要加入序列化方式等信息 不能只输入序列化的数据   解码器读到了之后才能调用对应函数来解码

在netty初始化中加入新的解码器和编码器 
## part2
加入本地缓存
