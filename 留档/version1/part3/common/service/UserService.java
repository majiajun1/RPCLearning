package version2.part3.common.service;

import version2.part3.common.pojo.User;

public interface UserService {
     User getUserByUserId(Integer id);
    Integer insertUserId(User user);
}
