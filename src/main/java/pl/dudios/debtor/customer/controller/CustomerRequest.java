package pl.dudios.debtor.customer.controller;

public record CustomerRequest(
        String name,
        String email,
        Integer age,
        String password) {
}
