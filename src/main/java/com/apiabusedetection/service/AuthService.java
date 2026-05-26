package com.apiabusedetection.service;

import com.apiabusedetection.common.exception.AppException;
import com.apiabusedetection.common.exception.ErrorCode;
import com.apiabusedetection.config.JwtProperties;
import com.apiabusedetection.dto.request.AuthRequest;
import com.apiabusedetection.dto.request.IntrospectRequest;
import com.apiabusedetection.dto.response.AuthResponse;
import com.apiabusedetection.dto.response.IntrospectResponse;
import com.apiabusedetection.entity.User;
import com.apiabusedetection.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    JwtProperties jwtProperties;
    PasswordEncoder passwordEncoder;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(jwtProperties.getSignerKey().getBytes(StandardCharsets.UTF_8));
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (!user.isEnabled()) {
            throw new AppException(ErrorCode.USER_DISABLED);
        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return AuthResponse.builder()
                .token(generateToken(user))
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("apiabusedetection.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(jwtProperties.getSignerKey().getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return stringJoiner.toString();
        }

        var authorities = new LinkedHashSet<String>();
        user.getRoles().forEach(role -> {
            authorities.add("ROLE_" + role.getName());
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions().forEach(permission -> authorities.add(permission.getName()));
            }
        });

        authorities.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}
