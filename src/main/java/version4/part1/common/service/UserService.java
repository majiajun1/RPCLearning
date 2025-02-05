package version4.part1.common.service;

import version4.part1.common.pojo.User;

public interface UserService {
     User getUserByUserId(Integer id);
    Integer insertUserId(User user);
}
