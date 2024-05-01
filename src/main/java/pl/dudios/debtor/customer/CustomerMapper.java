package pl.dudios.debtor.customer;

import lombok.experimental.UtilityClass;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.CustomerDTO;

@UtilityClass
public class CustomerMapper {

    public CustomerDTO mapToCustomerDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getSurname(),
                customer.getEmail(),
                customer.getAge(),
                customer.getRole(),
                customer.getGender()
        );
    }
}
