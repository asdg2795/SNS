package com.mangezjs.sns.service;

import com.mangezjs.sns.exception.ErrorCode;
import com.mangezjs.sns.exception.SnsApplicationException;
import com.mangezjs.sns.model.Alarm;
import com.mangezjs.sns.model.User;
import com.mangezjs.sns.model.entity.UserEntity;
import com.mangezjs.sns.repository.AlarmEntityRepository;
import com.mangezjs.sns.repository.UserEntityRepository;
import com.mangezjs.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;
    private final AlarmEntityRepository alarmEntityRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUserName(String userName){
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is duplicated", userName)));
    }

    @Transactional
    public User join(String userName, String password){
        // 회원가입하려는 userName으로 회원가입된 user가 있는지
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        // 회원가입 진행 = user를 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    // TODO : implement
    public String login(String userName, String password){
        // 회원가입 여부 체크
        UserEntity userEntity =  userEntityRepository
                .findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // 비밀번호 체크
        if(!encoder.matches(password, userEntity.getPassword())){
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        return JwtTokenUtils.generatedToken(userName, secretKey, expiredTimeMs);

    }

    // TODO : alarm return
    public Page<Alarm> alarmList(String userName, Pageable pageable){
        UserEntity userEntity =  userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        return alarmEntityRepository.findAllByUser(userEntity, pageable)
                .map(Alarm::fromEntity);

    }
}
