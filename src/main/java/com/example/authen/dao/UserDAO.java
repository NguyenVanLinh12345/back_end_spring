package com.example.authen.dao;

import com.example.authen.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class UserDAO {
    @Autowired
    SessionFactory sessionFactory;

    @SuppressWarnings({"unlocked", "rawTypes"})
    public User loadUserByUserName(final String username) {
        Session session = this.sessionFactory.getCurrentSession();
        String hql = "from User where username = :username";
        Query query = session.createQuery(hql);
        query.setParameter("username", username);

        List<User> users = query.list();

        if (users != null && users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings({"unlocked", "rawTypes"})
    public boolean checkLogin(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        String hql = "from User where username = :username and password = :password";
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("username", user.getUsername());
        query.setParameter("password", user.getPassword());

        List<User> users = query.list();

        if (users != null && users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
