package com.example.authen.controller;

import com.example.authen.model.User;
import com.example.authen.service.JwtService;
import com.example.authen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        String result = "";
        HttpStatus httpStatus;
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        try {
            if (userService.checkLogin(user)) {
                result = jwtService.generateTokenLogin(user.getUsername());
                httpStatus = HttpStatus.OK;
            } else {
                result = "Wrong username and password";
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception e) {
            result = "Server error";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, httpStatus);
    }
}