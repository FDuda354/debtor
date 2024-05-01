package pl.dudios.debtor.customer.model;

public record CustomerDTO(
        Long id,
        String firstName,
        String surname,
        String email,
        Integer age,
        Role role,
        Gender gender
) {
}
