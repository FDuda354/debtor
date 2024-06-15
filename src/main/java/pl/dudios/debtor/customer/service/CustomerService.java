package pl.dudios.debtor.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.customer.controller.CustomerRequest;
import pl.dudios.debtor.customer.images.model.Image;
import pl.dudios.debtor.customer.images.service.ImageService;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.Role;
import pl.dudios.debtor.customer.repository.CustomerDao;
import pl.dudios.debtor.exception.DuplicateResourceException;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerDao customerDao;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    //    public Page<CustomerDTO> getCustomers(int page, int size) {
//        Page<Customer> customers = customerDao.getCustomers(page, size);
//        List<CustomerDTO> customerDTOs = customers.getContent()
//                .stream()
//                .map(CustomerMapper::mapToCustomerDTO)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(customerDTOs, PageRequest.of(page, size), customers.getTotalElements());
//    }
//
    public Customer getCustomerById(final Long id) {
        return customerDao.getCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
    }

    public Customer addCustomer(CustomerRequest request) {

        if (customerDao.existsByEmail(request.email().toLowerCase())) {
            throw new DuplicateResourceException("Email already taken");
        }
        return customerDao.insertCustomer(Customer.builder()
                .firstName(request.firstName())
                .surname(request.surname())
                .email(request.email().toLowerCase())
                .age(request.age())
                .password(passwordEncoder.encode(request.password()))
                .gender(request.gender())
                .role(Role.ROLE_USER)
                .accountNonLocked(true)
                .enabled(true) //TODO dorobić
                .build());
    }

    public void deleteCustomerById(final Long id) {
        if (!customerDao.existsById(id)) {
            throw new ResourceNotFoundException("Customer with id: " + id + " not found");
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Long id, CustomerRequest request) {
        Customer oldCustomer = customerDao.getCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
        validateRequest(request, oldCustomer);

        customerDao.updateCustomer(oldCustomer);
    }

    private void validateRequest(CustomerRequest request, Customer oldCustomer) {

        boolean changed = false;

        if (request.email() != null && !request.email().isBlank() && !request.email().equalsIgnoreCase(oldCustomer.getEmail())) {
            if (customerDao.existsByEmail(request.email().toLowerCase()) && !request.email().equalsIgnoreCase(oldCustomer.getEmail())) {
                throw new DuplicateResourceException("Email already taken");
            }
            oldCustomer.setEmail(request.email().trim().toLowerCase());
            changed = true;
        }
        if (request.firstName() != null && !request.firstName().isBlank() && !request.firstName().equals(oldCustomer.getFirstName())) {
            oldCustomer.setFirstName(request.firstName().trim());
            changed = true;
        }
        if (request.surname() != null && !request.surname().isBlank() && !request.surname().equals(oldCustomer.getSurname())) {
            oldCustomer.setSurname(request.surname().trim());
            changed = true;
        }
        if (request.gender() != null && !request.gender().equals(oldCustomer.getGender())) {
            oldCustomer.setGender(request.gender());
            changed = true;
        }
        if (request.age() != null && !request.age().equals(oldCustomer.getAge())) {
            oldCustomer.setAge(request.age());
            changed = true;
        }

        if (!changed)
            throw new RequestValidationException("No changes provided");
    }

    public void addProfileImage(Long id, MultipartFile profileImage) {
        Customer customer = customerDao.getCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
        Image image = imageService.uploadImage(profileImage);
        customer.setProfileImage(image.getFileName());
        customerDao.updateCustomer(customer);
    }
}
