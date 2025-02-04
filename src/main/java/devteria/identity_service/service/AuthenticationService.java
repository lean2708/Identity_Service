package devteria.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import devteria.identity_service.dto.request.AuthenticationRequest;
import devteria.identity_service.dto.request.IntrospectRequest;
import devteria.identity_service.dto.response.AuthenticationResponse;
import devteria.identity_service.dto.response.IntrospectResponse;
import devteria.identity_service.entity.User;
import devteria.identity_service.exception.AppException;
import devteria.identity_service.exception.ErrorCode;
import devteria.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY; // KEY ngau nhien
    public AuthenticationResponse authenticated(AuthenticationRequest authenticationRequest) throws AppException, JOSEException {
        User userDB =  userRepository.findByUsername(authenticationRequest.getUsername()).
                orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(),userDB.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(userDB);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    //Tao JWT
    public String generateToken(User user) throws JOSEException {
        //Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //Payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().
                subject(user.getUsername())
                .issuer("AnB52")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli())) //hieu luc token
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        // Ki de tao jwt
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

        return jwsObject.serialize();
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        //if(!CollectionUtils.isEmpty(user.getRoles())){
          //  user.getRoles().forEach(s -> stringJoiner.add(s));
        //}
        return stringJoiner.toString();
    }

    // Xac thuc JWT
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expityTime.after(new Date()))
                .build();
    }

}
