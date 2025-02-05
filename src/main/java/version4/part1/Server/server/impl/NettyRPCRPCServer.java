package version4.part1.Server.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import version4.part1.Server.netty.nettyInitializer.NettyServerInitializer;
import version4.part1.Server.provider.ServiceProvider;
import version4.part1.Server.server.RpcServer;

@AllArgsConstructor
public class NettyRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        // netty 服务线程组boss负责建立连接， work负责具体的请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        System.out.println("netty服务端启动了");
        try {
            //启动netty服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //初始化
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    //NettyClientInitializer这里 配置netty对消息的处理机制
                    .childHandler(new NettyServerInitializer(serviceProvider));
            //同步堵塞
            ChannelFuture channelFuture=serverBootstrap.bind(port).sync();
            //sync() 是 ChannelFuture 类中的一个方法，用于同步等待异步操作完成。具体来说，它会阻塞当前线程，直到 bind() 操作完成。
            //由于 bind() 方法是异步的，sync() 会确保在绑定端口完成后，程序才会继续执行。
            //死循环监听
            channelFuture.channel().closeFuture().sync();
            //sync() 方法的作用是 阻塞当前线程，
            // 直到 closeFuture 完成，也就是说 服务器的通道关闭后，sync() 才会返回。
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}