package pl.dudios.debtor.customer.model;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        Integer age,
        Role role
) {
}
