package org.example.app.service;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    private Logger logger = Logger.getLogger(LoginService.class);
    private LoginRepository loginRepository = LoginRepository.getInstance();


    public boolean authenticate(LoginForm loginForm){
        List<LoginForm> authList = loginRepository.getLoginAll();
        logger.info("try auth with user-form: "+ loginForm);
        if(authList.size()!=0) {
            for(int i = 0; i < authList.size(); i++) {
                if (authList.get(i).getUsername().equals(loginForm.getUsername()) &&
                        authList.get(i).getPassword().equals(loginForm.getPassword())) {
                    logger.info("authList size = " + authList.size() + " first note of authList = " + authList.get(0));
                    return true;
                }
            }
        }
        else if((loginForm.getUsername().equals("root") && loginForm.getPassword().equals("123"))){
            logger.info("you enter root");
            return true;
        }
        else if(authList.size()==0 && loginForm.getUsername().equals("") && loginForm.getPassword().equals("")){
            //logger.info("ELSE authList size = " + authList.size() + " first note of authList = " + authList.get(0));
            return false;
        }
        return false;
    }
    public List<LoginForm> getAllLogin(){
        return loginRepository.getLoginAll();
    }

    public boolean registration(LoginForm loginForm){
        logger.info("try auth with user-form: "+ loginForm);
//        if(loginForm.getUsername().isEmpty() && loginForm.getPassword().isEmpty())
        if(loginRepository.addLogin(loginForm))
            return true;
        else
            return false;
    }

}
