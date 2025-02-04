package version1.part2.Client.proxy;

import lombok.AllArgsConstructor;
import version1.part2.Client.IOClient;
import version1.part2.Client.rpcClient.RpcClient;
import version1.part2.Client.rpcClient.impl.NettyRpcClient;
import version1.part2.Client.rpcClient.impl.SimpleSocketRpcCilent;
import version1.part2.common.Message.RpcRequest;
import version1.part2.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
@AllArgsConstructor
public class ClientProxy implements InvocationHandler{
     private RpcClient rpcClient;
    public ClientProxy(String host,int port,int choose){
        switch (choose){
            case 0:
                rpcClient=new NettyRpcClient(host,port);
                break;
            case 1:
                rpcClient=new SimpleSocketRpcCilent(host,port);  //初始化client  //part2 优化掉了port和host成员
        }
    }
    public ClientProxy(String host,int port){
        rpcClient=new NettyRpcClient(host,port);
    }

    @Override
     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request= RpcRequest.builder()
                .inferfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramTypes(method.getParameterTypes()).build();
        //IOClient.sendRequest 和服务端进行数据传输
        RpcResponse response= rpcClient.sendRequest(request);
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;   //创建代理对象
        //这里把创建过程整合了  用函数就能创建代理对象
        // 一般是
        //        HelloService proxy = (HelloService) Proxy.newProxyInstance(
        //            helloService.getClass().getClassLoader(),  // 目标对象的类加载器
        //            helloService.getClass().getInterfaces(),   // 目标对象的接口
        //            handler  // InvocationHandler
        //        );
    }
}
