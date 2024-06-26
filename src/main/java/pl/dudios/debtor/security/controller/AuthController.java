package pl.dudios.debtor.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.security.model.AuthRequest;
import pl.dudios.debtor.security.model.AuthResponse;
import pl.dudios.debtor.security.model.ResetPassRequest;
import pl.dudios.debtor.security.service.LoginService;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = loginService.login(new AuthRequest(
                authRequest.username().toLowerCase(),
                authRequest.password())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PostMapping("/password")
    public ResponseEntity<AuthResponse> changePassword(@RequestBody ResetPassRequest request,
                                                       @AuthenticationPrincipal Customer customer) {
        loginService.changePassword(new ResetPassRequest(
                request.newPassword(),
                customer)
        );

        return ResponseEntity.noContent().build();
    }
}
