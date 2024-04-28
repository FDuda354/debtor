package pl.dudios.debtor.security.model;

import pl.dudios.debtor.customer.model.CustomerDTO;

public record AuthResponse(
        String token,
        CustomerDTO customerDTO
) {
}
