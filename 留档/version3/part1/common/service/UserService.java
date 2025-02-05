package version3.part1.common.service;

import version3.part1.common.pojo.User;

public interface UserService {
     User getUserByUserId(Integer id);
    Integer insertUserId(User user);
}
