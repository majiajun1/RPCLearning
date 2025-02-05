package version3.part1.Server.provider;

import version3.part1.Server.serviceRegister.ServiceRegister;
import version3.part1.Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {
    //集合中存放服务的实例
    private Map<String,Object> interfaceProvider;
    private int port;
    private String host;

    private ServiceRegister serviceRegister;

    public ServiceProvider(String host, int port){
        this.host = host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
    }
    //本地注册服务

    public void provideServiceInterface(Object service,boolean canRetry){
        String serviceName=service.getClass().getName();
        Class<?>[] interfaceName=service.getClass().getInterfaces();

        for (Class<?> clazz:interfaceName){
             interfaceProvider.put(clazz.getName(),service);
             serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port),canRetry);   // 服务多个接口的实现（类）：服务的接口地址
        }

    }
    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}