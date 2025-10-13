package com.demo.controller;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

/**
 *
 * @author MINHHUNG
 */
public class resetService {
    private final int LIMIT_MINUS = 10;
    static final String from = "leminhhungqt2005@gmail.com";
    static final String password = "sail vlmg zfpr lxnf";
//   static final String to = "minhkhangvoz25@gmail.com";
    
    public String generateToken(){
        return UUID.randomUUID().toString();
    }
    
    public LocalDateTime expireDatetime(){
        return LocalDateTime.now().plusMinutes(LIMIT_MINUS);
        
    }
    
    public boolean isExpireTime(LocalDateTime time){
        return LocalDateTime.now().isAfter(time);
    }
//    public static void main(String[] args) {
//        final String from = "leminhhungqt2005@gmail.com";
//        final String password = "wyzn jedk wbco vgpk";
//        //Khai báo các thuộc tính
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");//SMTP host
//        props.put("mail.smtp.port", "587"); //TLS:587; SSL:465
//        props.put("mail.smtp.auth", "true"); // true, cần phải đăng nhập 
//        props.put("mail.smtp.starttls.enable", "true");//protocal TLS
//        
//        //Create Authenticator (lấy ra đc authenticator để đăng nhập vào gmail)
//        Authenticator auth = new Authenticator() { 
//            @Override // phương thức
//            protected PasswordAuthentication getPasswordAuthentication(){
//                return new PasswordAuthentication(from, password);
//            }
//        };
//        // Tạo phiên gửi mail
//        Session session = Session.getInstance(props,auth); // đăng nhập vào gmail với tài khoản authen
//        final String to = "minhkhangvoz25@gmail.com";
//        // Soạn email để gửi
//        MimeMessage msg = new MimeMessage(session); // tạo 1 message mới
//        //đôi lúc ko connect đc nên phải để vào try catch
//        try {
//            //Kiểu nội dung
//            msg.addHeader("Content-type", "text/html; charset=UTF-8"); // 
//            msg.setFrom(from);
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false)); // người nhận 
//            msg.setSubject("Reset Password","UTF-8"); // Tiêu đề
//            String content = "<h1>Hello</h1>"+"<p>Click the link to reset password "
//                    + "<a>Click here</a></p>";
//            msg.setContent(content,"text/html; charset=UTF-8");
//            Transport.send(msg);//gửi mail
//            System.out.println("Send sucessfully");
//            
//        } catch (Exception e) {
//            System.out.println("Send error");
//            e.printStackTrace();
//            
//        }
//    }
//}
    public boolean sendEmail(String to, String link, String name){
        
        //Khai báo các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");//SMTP host
        props.put("mail.smtp.port", "587"); //TLS:587; SSL:465
        props.put("mail.smtp.auth", "true"); // true, cần phải đăng nhập 
        props.put("mail.smtp.starttls.enable", "true");//protocal TLS
        
        //Create Authenticator (lấy ra đc authenticator để đăng nhập vào gmail)
        Authenticator auth = new Authenticator() { 
            @Override // phương thức
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from, password);
            }
        };
        // Tạo phiên gửi mail
        Session session = Session.getInstance(props,auth); // đăng nhập vào gmail với tài khoản authen
        session.setDebug(true);  // 👈 thêm dòng này
        // Soạn email để gửi
        MimeMessage msg = new MimeMessage(session); // tạo 1 message mới
        //đôi lúc ko connect đc nên phải để vào try catch
        try {
            //Kiểu nội dung
            msg.addHeader("Content-type", "text/html; charset=UTF-8"); // 
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false)); // người nhận 
            msg.setSubject("Reset Password","UTF-8"); // Tiêu đề
            String content = "<h1>Hello" + name + "</h1>"+"<p>Click the link to reset password "
                    + "<a href="+link+">Click here</a></p>";
            msg.setContent(content,"text/html; charset=UTF-8");
            Transport.send(msg);//gửi mail
            System.out.println("Send sucessfully");
            return true;
        } catch (Exception e) {
            System.out.println("Send error");
            e.printStackTrace();
            return false;
        }
    }
}