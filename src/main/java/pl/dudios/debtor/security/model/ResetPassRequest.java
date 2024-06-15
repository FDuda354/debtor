package pl.dudios.debtor.security.model;

import pl.dudios.debtor.customer.model.Customer;

public record ResetPassRequest(
        String newPassword,
        Customer customer
) {
}
