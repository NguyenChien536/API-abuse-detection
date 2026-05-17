package com.apiabusedetection.auth.service;

import com.apiabusedetection.auth.dto.request.AuthRequest;
import com.apiabusedetection.auth.dto.request.IntrospectRequest;
import com.apiabusedetection.auth.dto.response.AuthResponse;
import com.apiabusedetection.auth.dto.response.IntrospectResponse;
import com.apiabusedetection.common.exception.AppException;
import com.apiabusedetection.common.exception.ErrorCode;
import com.apiabusedetection.user.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthService {
    final UserRepository userRepository;

    @NonFinal
    protected static final String SIGNER_KEY = "374ccce55ce533f7dcc8bc02b136d0b5963d0ed654f532313e1e49862fe68c11";
    private final LogbackMetrics logbackMetrics;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public AuthResponse authenticate(AuthRequest request){
        var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPasswordHash());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(request.getUsername());
        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("apiabusedetection.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("customClaim", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject  jwsObject = new JWSObject(header, payload);

        try {//ky
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
