package com.github.ideepakkalra.eventmanagement.services;

import com.github.ideepakkalra.eventmanagement.db.LoginRepository;
import com.github.ideepakkalra.eventmanagement.entity.Login;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.exceptions.AccountLockedException;
import com.github.ideepakkalra.eventmanagement.exceptions.AccountNotFoundException;
import com.github.ideepakkalra.eventmanagement.exceptions.InvalidCredentialsException;
import com.github.ideepakkalra.eventmanagement.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    public User login(LoginRequest loginRequest) throws Exception {
        Login login = loginRepository.findById(loginRequest.getPhoneNumber()).orElseThrow(AccountNotFoundException::new);
        if (login.getStatus() == Login.Status.LOCKED) {
            throw new AccountLockedException();
        } else if (!login.getPasscode().equals(loginRequest.getPasscode())) {
            login.setRetryCount(login.getRetryCount() + 1);
            login.setStatus(Login.Status.FAILURE);
            if (login.getRetryCount() >= 5) {
                login.setStatus(Login.Status.LOCKED);
            }
            loginRepository.save(login);
            throw new InvalidCredentialsException();
        }
        login.setRetryCount(0);
        login.setStatus(Login.Status.SUCCESS);
        login.setLoginDate(new Date());
        loginRepository.save(login);
        return login.getUser();
    }

    public User logout(LoginRequest loginRequest) throws Exception  {
        Login login = loginRepository.findById(loginRequest.getPhoneNumber()).orElseThrow(AccountNotFoundException::new);
        login.setLogoutDate(new Date());
        loginRepository.save(login);
        return login.getUser();
    }
}
