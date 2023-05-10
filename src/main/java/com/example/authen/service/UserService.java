package com.example.authen.service;
import com.example.authen.dao.UserDAO;
import com.example.authen.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public User loadUserByUsername(final String username) {
        return userDAO.loadUserByUserName(username);
    }

    public boolean checkLogin(User user) {
        return userDAO.checkLogin(user);
    }
}
