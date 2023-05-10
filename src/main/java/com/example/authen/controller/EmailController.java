package com.example.authen.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

// cai nay de gui mail duoi dang html
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

class MyEmail{
	public String typeUser, title, content;
}

@RestController
@RequestMapping(value = "/send-email", produces = "application/json")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private JavaMailSender emailSender;

    @PostMapping(consumes = "application/json")
    public Map<String, String> sendEmail(@RequestBody MyEmail myEmail) {    	
        SimpleMailMessage message = new SimpleMailMessage();   
        
        String[] arr = {"linhamtv@gmail.com", "bakhangpham30@gmail.com"};
        
        message.setSubject(myEmail.title);
        message.setText(myEmail.content);
        message.setTo(arr[1]);
        emailSender.send(message);        	

    	Map<String, String> response = new HashMap<String, String>();
        response.put("oke", "oke");
        return response;
    }
    
    @PostMapping("/html")
    public Map<String, String> sendEmailHtml(@RequestBody MyEmail myEmail) throws MessagingException {        
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String[] arr = {"linhnamtv@gmail.com", "bakhangpham30@gmail.com"};
        helper.setSubject(myEmail.title);
        helper.setText(myEmail.content, true);

        for(String x: arr) {
            helper.setTo(x);
            emailSender.send(message);        	
        }
        Map<String, String> response = new HashMap<String, String>();
        response.put("oke", "oke");

        return response;
    }
}