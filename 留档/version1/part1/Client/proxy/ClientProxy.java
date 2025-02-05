package version2.part1.Client.proxy;

import version2.part1.Client.IOClient;
import version2.part1.common.Message.RpcRequest;
import version2.part1.common.Message.RpcResponse;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
@AllArgsConstructor
public class ClientProxy implements InvocationHandler{
    private String host;
    private int port;

    @Override
     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request=RpcRequest.builder()
                .inferfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramTypes(method.getParameterTypes()).build();
        //IOClient.sendRequest 和服务端进行数据传输
        RpcResponse response= IOClient.sendRequest(host,port,request);
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
