package com.github.ideepakkalra.eventmanagement.services;

import com.github.ideepakkalra.eventmanagement.db.LoginRepository;
import com.github.ideepakkalra.eventmanagement.entity.Login;
import com.github.ideepakkalra.eventmanagement.exceptions.AccountLockedException;
import com.github.ideepakkalra.eventmanagement.exceptions.AccountNotFoundException;
import com.github.ideepakkalra.eventmanagement.exceptions.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public Login login(Login login) throws Exception {
        Login loginEntity = loginRepository.findById(login.getPhoneNumber()).orElseThrow(AccountNotFoundException::new);
        if (loginEntity.getStatus() == Login.Status.LOCKED) {
            throw new AccountLockedException();
        } else if (!login.getPasscode().equals(loginEntity.getPasscode())) {
            loginEntity.setRetryCount(loginEntity.getRetryCount() + 1);
            loginEntity.setStatus(Login.Status.FAILURE);
            if (loginEntity.getRetryCount() >= 5) {
                loginEntity.setStatus(Login.Status.LOCKED);
            }
            loginRepository.save(loginEntity);
            throw new InvalidCredentialsException();
        }
        loginEntity.setRetryCount(0);
        loginEntity.setStatus(Login.Status.SUCCESS);
        loginEntity.setLoginDate(new Date());
        loginRepository.save(loginEntity);
        return loginEntity;
    }

    public Login logout(Login login) throws Exception  {
        login = loginRepository.findById(login.getPhoneNumber()).orElseThrow(AccountNotFoundException::new);
        login.setLogoutDate(new Date());
        loginRepository.save(login);
        return login;
    }

    public Login create(Login login) {
        return loginRepository.save(login);
    }

    public Login selectByPhoneNumber(String phoneNumber) {
        return loginRepository.findByPhoneNumber(phoneNumber);
    }

    public Login update(Login login) {
        return loginRepository.save(login);
    }
}
