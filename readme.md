# version3
## version3.part1

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

# version3
实现自定义编码器解码器 

建立zookeeper本地缓存

## part1
没啥难的 就是文档没有说一些小变化 导致找BUG花了很多时间

定义好了 新的序列化器后 然后定义新的解码器和编码器

编码器要加入序列化方式等信息 不能只输入序列化的数据   解码器读到了之后才能调用对应函数来解码

在netty初始化中加入新的解码器和编码器 
## part2
加入本地缓存

有点像redis的哨兵模式 
最主要的是watcher是更新缓存的 不是缓存没有往里加   而是缓存的服务地址有变化。

Zookeeper 的 Watcher 机制由 客户端、Zookeeper 服务器、Watcher 事件 三部分组成：

客户端（Client）：向 Zookeeper 服务器注册 Watcher。
Zookeeper 服务器（Server）：维护 Watcher 并在节点发生变化时触发通知。
Watcher 事件（WatchedEvent）：服务器检测到变化后，发送事件通知客户端。

1. 客户端注册 Watcher  ——>  2. 服务器存储 Watcher  
        (getData, exists, getChildren)
        
3. 节点发生变化  ——>  4. 服务器触发 Watcher 事件  
        
5. 客户端收到事件  ——>  6. 客户端回调处理  
       (Watcher 触发 process 方法)

# version3
多了白名单  超时重传 负载均衡

## part1

loadbalance接口  管理节点 来均衡负载
实现了三种均衡方法   轮询  哈希  随机

哈希一致性有点复杂  主要缺点是哈希冲突 

传统哈希：
当服务器数量变动时，几乎所有数据都需要重新映射！
如果 N 变成 N+1，大部分 key 的存储位置都会发生变化，导致大量数据迁移，影响系统性能。


一致性哈希将所有服务器和数据键（keys）映射到 一个固定长度的哈希环（0 ~ 2³²-1），通过哈希函数 hash(node) 计算服务器节点的位置，并使用 hash(key) 计算数据 key 在环上的位置。

存储规则：

按照顺时针方向，key 存储到第一个大于等于 hash(key) 的服务器节点上。
如果到达环的末端（最大哈希值），则回到环的起点，存储到第一个服务器上。

引入虚拟节点  一致性哈希大幅减少数据迁移

每个真实节点都会引入固定数量的虚拟节点  分布在不同位置


## part2
超时重试  用guava retry

幂等概念：一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同

白名单的作用就是只重试幂等的服务 

重点在于哪些服务可以重试的  把这些服务放在一个地方去（白名单）
对应的 就要修改注册中心  设置是否可重试

# version4
限流与熔断

客户端和服务端各设置一个 熔断器/限流器 防止请求过多

## part1
限流   服务端
常见的四种限流算法：计数器法 滑动窗口算法  漏桶算法  令牌桶算法
