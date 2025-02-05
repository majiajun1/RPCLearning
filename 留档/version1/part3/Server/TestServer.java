package version2.part3.Server;

import version2.part3.Server.provider.ServiceProvider;
import version2.part3.Server.server.RpcServer;
import version2.part3.Server.server.impl.NettyRPCRPCServer;
import version2.part3.Server.server.impl.ThreadPoolRPCRPCServer;
import version2.part3.common.service.Impl.UserServiceImpl;
import version2.part3.common.service.UserService;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("localhost", 9999);
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new NettyRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}