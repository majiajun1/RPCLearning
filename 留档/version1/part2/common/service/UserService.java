package version2.part2.common.service;

import version2.part2.common.pojo.User;

public interface UserService {
     User getUserByUserId(Integer id);
    Integer insertUserId(User user);
}
