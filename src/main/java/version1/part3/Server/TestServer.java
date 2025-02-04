package version1.part3.Server;

import version1.part2.Server.provider.ServiceProvider;
import version1.part2.Server.server.RpcServer;
import version1.part2.Server.server.impl.NettyRPCRPCServer;
import version1.part2.Server.server.impl.ThreadPoolRPCRPCServer;
import version1.part2.common.service.Impl.UserServiceImpl;
import version1.part2.common.service.UserService;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new NettyRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}