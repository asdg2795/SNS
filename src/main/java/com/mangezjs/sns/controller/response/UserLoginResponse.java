package com.mangezjs.sns.controller.response;

import com.mangezjs.sns.model.User;
import com.mangezjs.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String token;
}
