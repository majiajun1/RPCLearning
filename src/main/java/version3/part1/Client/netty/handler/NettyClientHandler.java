package version3.part1.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import version3.part1.common.Message.RpcResponse;

/*来处理 RPC 响应（RpcResponse）。在这个类中，我们主要处理了两种事件：接收到响应和异常捕获。
ctx 是 ChannelHandlerContext，提供了对 Channel 的操作权限。
通过 AttributeKey，你可以在 Channel 上存储和检索数据。
当你在 Channel 上存储某些数据时，你需要使用一个 AttributeKey 来标识这些数据，这样可以避免多个属性之间的命名冲突。
valueOf("RPCResponse")：通过 valueOf 方法创建一个 AttributeKey，它使用字符串 "RPCResponse" 作为键名。
这里 "RPCResponse" 是用于唯一标识该属性的名称（键）。你可以根据需要使用任何字符串，只要它是唯一的，能够标识该属性。
*/

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        // 接收到response, 给channel设计别名，让sendRequest里读取response
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(response);
        //将 RpcResponse 对象存储到当前 Channel 上，属性的键是 "RPCResponse"，意味着你可以通过 key 获取并使用 response。
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常处理
        cause.printStackTrace();
        ctx.close();
    }
}