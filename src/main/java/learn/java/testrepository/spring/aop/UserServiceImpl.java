package learn.java.testrepository.spring.aop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Map<Long, User> userMap = new ConcurrentHashMap<>();
    private static Long userId = 0L;

    public User createUser(final String name) {
        final User user = new User(userId++, name);
        userMap.put(user.getId(), user);
        return user;
    }
}
