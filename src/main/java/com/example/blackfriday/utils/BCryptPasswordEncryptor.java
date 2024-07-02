package com.example.blackfriday.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public final class BCryptPasswordEncryptor implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        Assert.hasText((String) rawPassword, "비밀번호를 입력해주세요.");
        return BCrypt.hashpw((String) rawPassword,BCrypt.gensalt());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        Assert.hasText((String) rawPassword, "비밀번호를 입력해주세요.");
        Assert.hasText((String) rawPassword, "암호화된 비밀번호는 null일 수 없습니다.");
        return BCrypt.checkpw((String) rawPassword, encodedPassword);
    }
}
