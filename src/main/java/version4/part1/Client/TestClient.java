package version4.part1.Client;

import version4.part1.Client.proxy.ClientProxy;
import version4.part1.common.pojo.User;
import version4.part1.common.service.UserService;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy=new ClientProxy(); //part2多了switch选择条件
        UserService proxy=clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());

        User u= User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}