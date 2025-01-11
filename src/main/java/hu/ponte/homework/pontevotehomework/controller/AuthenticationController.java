package hu.ponte.homework.pontevotehomework.controller;


import hu.ponte.homework.pontevotehomework.dto.income.AuthenticationRequest;
import hu.ponte.homework.pontevotehomework.dto.income.RegisterRequest;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AuthenticationResponse;
import hu.ponte.homework.pontevotehomework.service.AuthService;
import hu.ponte.homework.pontevotehomework.validator.AuthenticationRequestValidator;
import hu.ponte.homework.pontevotehomework.validator.RegisterRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;
    private final RegisterRequestValidator registerRequestValidator;
    private final AuthenticationRequestValidator authenticationRequestValidator;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public AuthenticationController(AuthService authService, RegisterRequestValidator registerRequestValidator, AuthenticationRequestValidator authenticationRequestValidator) {
        this.authService = authService;
        this.registerRequestValidator = registerRequestValidator;
        this.authenticationRequestValidator = authenticationRequestValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Post register request: {}", request);
        AuthenticationResponse authenticationResponse = authService.register(request);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest command,
                                                               HttpServletRequest request) {
        log.info("Post authenticate request: {}", command);
        return ResponseEntity.ok(authService.authenticate(command, request));
    }

    @InitBinder("registerRequest")
    protected void initBinderOfDeliverCommand(WebDataBinder binder) {
        binder.addValidators(registerRequestValidator);
    }

    @InitBinder("authenticationRequest")
    protected void initBinderOfAuthenticationRequest(WebDataBinder binder) {
        binder.addValidators(authenticationRequestValidator);
    }
}
