package version4.part1.Server;

import version4.part1.Server.provider.ServiceProvider;
import version4.part1.Server.server.RpcServer;
import version4.part1.Server.server.impl.NettyRPCRPCServer;
import version4.part1.common.service.Impl.UserServiceImpl;
import version4.part1.common.service.UserService;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("localhost", 9999);
        serviceProvider.provideServiceInterface(userService,true);

        RpcServer rpcServer=new NettyRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}