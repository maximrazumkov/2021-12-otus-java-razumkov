package ru.otus.services;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dao.UserDao;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public class DbLoginServiceImpl extends AbstractLoginService {

    private static final Logger log = LoggerFactory.getLogger(DbLoginServiceImpl.class);

    private final UserDao userDao;

    public DbLoginServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal userPrincipal) {
        return List.of(new RolePrincipal("user"));
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        log.info(String.format("User(%s)", login));
        Optional<User> dbUser = userDao.findByLogin(login);
        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
    }
}
