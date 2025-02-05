package version4.part1.Client.rpcClient.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import version4.part1.Client.netty.nettyInitializer.NettyClientInitializer;
import version4.part1.Client.rpcClient.RpcClient;
import version4.part1.Client.serviceCenter.ServiceCenter;
import version4.part1.Client.serviceCenter.ZKServiceCenter;
import version4.part1.common.Message.RpcRequest;
import version4.part1.common.Message.RpcResponse;


import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {
//    private String host;
//    private int port;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

     private ServiceCenter serviceCenter;
    public NettyRpcClient() throws InterruptedException {
        this.serviceCenter=new ZKServiceCenter();
    }
    //netty客户端初始化
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup) //将eventLoopGroup设置为客户端的I/O线程池
                .channel(NioSocketChannel.class)  //指定了 Netty 客户端使用的 通道类型，这里使用的是 NioSocketChannel。
                //NettyClientInitializer这里 配置netty对消息的处理机制
                .handler(new NettyClientInitializer());  //初始化netty
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        InetSocketAddress address = serviceCenter.serviceDiscovery(request.getInferfaceName());//让zookeeper提供地址
         String host = address.getHostName();
        int port = address.getPort();

        try {

            //创建一个channelFuture对象，代表这一个操作事件，sync方法表示堵塞直到connect完成
            ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
            //channel表示一个连接的单位，类似socket
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            //sync()堵塞获取结果
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 当前场景下选择堵塞获取结果
            // 其它场景也可以选择添加监听器的方式来异步获取结果 channelFuture.addListener...
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();

            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
