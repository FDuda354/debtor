package pl.dudios.debtor.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.CustomerDTO;
import pl.dudios.debtor.customer.service.CustomerService;
import pl.dudios.debtor.security.jwt.JwtUtil;
import pl.dudios.debtor.security.model.AuthRequest;
import pl.dudios.debtor.security.model.AuthResponse;
import pl.dudios.debtor.security.model.ResetPassRequest;
import pl.dudios.debtor.utils.mappers.CustomerMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
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

    @Transactional
    public void changePassword(ResetPassRequest request) {
        Customer customer = customerService.getCustomerById(request.customer().getId());
        customer.setPassword(passwordEncoder.encode(request.newPassword()));
    }
}
