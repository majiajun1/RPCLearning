package version2.part3.Server.serviceRegister;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    //  注册：保存服务与地址。
    void register(String serviceName, InetSocketAddress serviceAddress);

}