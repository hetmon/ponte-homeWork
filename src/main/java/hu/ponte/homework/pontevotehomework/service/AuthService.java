package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.dto.UserMapper;
import hu.ponte.homework.pontevotehomework.dto.income.AuthenticationRequest;
import hu.ponte.homework.pontevotehomework.dto.income.RegisterRequest;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AuthenticationResponse;
import hu.ponte.homework.pontevotehomework.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserMapper userMapper) {


        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
        User customer = userMapper.makeUser(request, passwordEncoder.encode(request.getPassword()));
        userRepository.save(customer);
        String token = jwtService.generateToken(customer);
        String fullName = request.getFirstName() + " " + request.getLastName();
        response.setMessage("Register was successful. Welcome " + fullName + " Now you can login");
        response.setToken(token);
        return response;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest command, HttpServletRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                command.getEmail(),
                command.getPassword()));
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        setUserDetailsIntSession(request, user.getId(), user.getEmail());
        response.setMessage("Login was successful. Welcome " + user.getFirstName() + " " + user.getLastName());
        response.setToken(token);
        return response;
    }

    private void setUserDetailsIntSession(HttpServletRequest request, Long userId, String email) {
        HttpSession session = request.getSession();
        session.setAttribute("userId", userId);
        session.setAttribute("email", email);
    }
}
