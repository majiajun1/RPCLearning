package version2.part1.Client.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {
    //  查询：根据服务名查找地址
    InetSocketAddress serviceDiscovery(String serviceName);
}
