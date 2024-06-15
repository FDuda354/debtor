package pl.dudios.debtor.customer.repository;

import pl.dudios.debtor.customer.model.Gender;
import pl.dudios.debtor.customer.model.Role;

public interface CustomerProjection {
    Long getId();

    String getFirstName();

    String getSurname();

    String getEmail();

    String getPassword();

    Integer getAge();

    Role getRole();

    Gender getGender();

    boolean isEnabled();

    boolean isAccountNonLocked();
}