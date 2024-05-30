package pl.dudios.debtor.customer.controller;

import pl.dudios.debtor.customer.model.Gender;

public record CustomerRequest(
        String firstName,
        String surname,
        String email,
        Integer age,
        Gender gender,
        String password
) {
}
