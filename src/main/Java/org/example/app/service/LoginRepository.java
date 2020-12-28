package org.example.app.service;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LoginRepository {
    private static volatile LoginRepository instans;
    private List<LoginForm> loginList;
    private final Logger logger = Logger.getLogger(LoginRepository.class);

    private LoginRepository(){
        loginList = new ArrayList<>();
    }

    public static LoginRepository getInstance(){
        if(instans == null){
            synchronized (LoginRepository.class){
                if(instans == null){
                    instans = new LoginRepository();
                }
            }
        }
        return instans;
    }

    public boolean addLogin(LoginForm loginForm){
        if(loginList.contains(loginForm)) {
            logger.info("this login is used by another user");
            return false;
        }
        else{
            loginList.add(loginForm);
            logger.info("username and password are successfully registered");
            return true;
        }
    }

    public boolean getLogin(String login, String password){
        for(LoginForm lF: loginList){
            if(lF.getUsername().equals(login) && lF.getPassword().equals(password)) {
                logger.info("User"+lF.getUsername()+"registered in the database");
                return true;
            }
        }
        logger.info("this user is not present in the database");
        return false;
    }
    public List<LoginForm> getLoginAll(){
        return new ArrayList<>(loginList);
    }
}

