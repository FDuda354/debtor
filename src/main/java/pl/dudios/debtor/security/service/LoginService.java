package pl.dudios.debtor.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.customer.CustomerMapper;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.CustomerDTO;
import pl.dudios.debtor.security.model.AuthRequest;
import pl.dudios.debtor.security.model.AuthResponse;
import pl.dudios.debtor.security.jwt.JwtUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest authRequest) {
        log.info("LoginService.login({})", authRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.username(),
                authRequest.password())
        );
        Customer principal = (Customer) authenticate.getPrincipal();
        log.info("getPrincipal() return: {}", authRequest);

        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDTO(principal);
        String token = jwtUtil.issueToken(customerDTO.email(), customerDTO.id(), customerDTO.role().name());

        return new AuthResponse(token, customerDTO);
    }
}
