package version1.part1.Server;

import version1.part1.Server.provider.ServiceProvider;
import version1.part1.Server.server.RpcServer;
import version1.part1.Server.server.impl.SimpleRPCRPCServer;
import version1.part1.Server.server.impl.ThreadPoolRPCRPCServer;
import version1.part1.common.service.Impl.UserServiceImpl;
import version1.part1.common.service.UserService;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new ThreadPoolRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}