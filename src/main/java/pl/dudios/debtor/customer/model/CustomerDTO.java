package pl.dudios.debtor.customer.model;

import pl.dudios.debtor.customer.images.model.Image;

public record CustomerDTO(
        Long id,
        String firstName,
        String surname,
        String email,
        Integer age,
        Role role,
        Gender gender,
        Image prfileImage
) {
}
