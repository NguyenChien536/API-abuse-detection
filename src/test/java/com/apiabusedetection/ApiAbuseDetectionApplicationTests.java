package com.apiabusedetection;

import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

@Slf4j
@SpringBootTest
class ApiAbuseDetectionApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void hash() throws Exception {
        String password = "123456";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte[] digest = md.digest();
        String md5Hash = DatatypeConverter.printHexBinary(digest);

        log.info("md5Hash round 1: {}", md5Hash);

        md.update(password.getBytes());
        digest = md.digest();
        md5Hash = DatatypeConverter.printHexBinary(digest);

        log.info("md5Hash round 2: {}", md5Hash);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        log.info("Bcrypt round 1: {}", passwordEncoder.encode(password));
        log.info("Bcrypt round 2: {}", passwordEncoder.encode(password));
    }
}
