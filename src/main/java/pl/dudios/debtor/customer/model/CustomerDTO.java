package pl.dudios.debtor.customer.model;

import java.util.List;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        Integer age,
        List<String> roles
) {
}
