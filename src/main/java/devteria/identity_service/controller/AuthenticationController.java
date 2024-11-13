package devteria.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import devteria.identity_service.dto.request.AuthenticationRequest;
import devteria.identity_service.dto.request.IntrospectRequest;
import devteria.identity_service.dto.response.ApiResponse;
import devteria.identity_service.dto.response.AuthenticationResponse;
import devteria.identity_service.dto.response.IntrospectResponse;
import devteria.identity_service.exception.AppException;
import devteria.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/token")
    private ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws AppException, JOSEException {
        AuthenticationResponse result = authenticationService.authenticated(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/introspect")
    private ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        IntrospectResponse result = authenticationService.introspect(request);

        ApiResponse<IntrospectResponse> response = new ApiResponse<>();
        response.setResult(result);

        return response;
    }

}
