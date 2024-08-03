package pl.dudios.debtor.customer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dudios.debtor.customer.repository.CustomerDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;
    @Spy
    private PasswordEncoder passwordEncoder;


    @Test
    void itShouldGetCustomers() {
        //When
        //TODO
//        underTest.getCustomers();
//
//        //Then
//        then(customerDao).should().getCustomers();
//        then(customerDao).shouldHaveNoMoreInteractions();
    }

//    @Test
//    void itShouldGetCustomerByIdIfPresent() {
//        //Given
//        Long id = 1L;
//        Customer customer = Customer.builder()
//                .id(id)
//                .name("Jan")
//                .email("email")
//                .age(20)
//                .build();
//
//        CustomerDTO expected = CustomerMapper.mapToCustomerDTO(customer);
//        given(customerDao.getCustomerById(any(Long.class))).willReturn(Optional.of(customer));
//
//        //When
//        CustomerDTO actual = underTest.getCustomerById(id);
//
//        //Then
//        then(customerDao).should().getCustomerById(id);
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(expected);
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//    @Test
//    void itShouldThrowWhenGetCustomerByIdIfNotPresent() {
//        //Given
//        Long id = 1L;
//        given(customerDao.getCustomerById(any(Long.class)))
//                .willReturn(Optional.empty());
//        //When
//        //Then
//        assertThatThrownBy(() -> underTest.getCustomerById(id))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("Customer with id: " + id + " not found");
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//    @Test
//    void itShouldAddCustomer() {
//        //Given
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//        CustomerRequest request = new CustomerRequest("Jan", "email@.gamil", 20, "password");
//        given(customerDao.existsByEmail(any(String.class))).willReturn(false);
//        //When
//
//        underTest.addCustomer(request);
//
//        //Then
//        then(customerDao).should().existsByEmail(request.email());
//        then(customerDao).should().insertCustomer(customerArgumentCaptor.capture());
//        assertThat(customerArgumentCaptor.getValue()).isEqualToIgnoringGivenFields(request, "id", "password");
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//    @Test
//    void itShouldNotAddCustomerWhenEmailIsTaken() {
//        //Given
//        CustomerRequest request = new CustomerRequest("Jan", "email@.gamil", 20, "password");
//        given(customerDao.existsByEmail(any(String.class))).willReturn(true);
//        //When
//        //Then
//        assertThatThrownBy(() -> underTest.addCustomer(request))
//                .isInstanceOf(DuplicateResourceException.class)
//                .hasMessageContaining("Email already taken");
//        then(customerDao).should(never()).insertCustomer(any(Customer.class));
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//    @Test
//    void itShouldDeleteCustomer() {
//        //Given
//        Long id = 1L;
//        given(customerDao.existsById(any(Long.class))).willReturn(true);
//        //When
//        underTest.deleteCustomerById(id);
//        //Then
//        then(customerDao).should().existsById(id);
//        then(customerDao).should().deleteCustomerById(id);
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//    @Test
//    void itShouldThrowWhenDeleteIfCustomerNotExist() {
//        //Given
//        Long id = 1L;
//        given(customerDao.existsById(any(Long.class))).willReturn(false);
//        //When
//        //Then
//        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("Customer with id: " + id + " not found");
//        then(customerDao).should().existsById(id);
//        then(customerDao).should(never()).deleteCustomerById(id);
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//
//    @Test
//    void itShouldUpdateCustomer() {
//        //Given
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//        Long id = 1L;
//        CustomerRequest request = new CustomerRequest("Updated Name", "updatedEmail@gmail.com", 30, "password");
//        Customer oldCustomer = Customer.builder()
//                .id(id)
//                .name("Old Name")
//                .email("oldEmail@gmail.com")
//                .age(25)
//                .password("password")
//                .build();
//
//        given(customerDao.getCustomerById(any(Long.class))).willReturn(Optional.of(oldCustomer));
//        given(customerDao.existsByEmail(any(String.class))).willReturn(false);
//
//        //When
//        underTest.updateCustomer(id, request);
//
//        //Then
//        then(customerDao).should().getCustomerById(id);
//        then(customerDao).should().existsByEmail(request.email());
//        then(customerDao).should().updateCustomer(customerArgumentCaptor.capture());
//        assertThat(customerArgumentCaptor.getValue()).isEqualToIgnoringGivenFields(request, "id");
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//    static Stream<Arguments> provideCorrectCustomerRequest() {
//        return Stream.of(
//                arguments(new CustomerRequest("Name", "email@example.com", 20, "password"), "Name", "email@example.com", 20),
//                arguments(new CustomerRequest(null, "email@example.com", 20, "password"), "Old Name", "email@example.com", 20),
//                arguments(new CustomerRequest("Name", null, 20, "password"), "Name", "oldEmail@gmail.com", 20),
//                arguments(new CustomerRequest("Name", "email@example.com", null, "password"), "Name", "email@example.com", 25),
//                arguments(new CustomerRequest("Name", null, null, "password"), "Name", "oldEmail@gmail.com", 25),
//                arguments(new CustomerRequest(null, "email@example.com", null, "password"), "Old Name", "email@example.com", 25),
//                arguments(new CustomerRequest(null, null, 20, "password"), "Old Name", "oldEmail@gmail.com", 20)
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideCorrectCustomerRequest")
//    void itShouldUpdateCustomer(CustomerRequest request, String expectedName, String expectedEmail, Integer expectedAge) {
//        //Given
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//        Long id = 10L;
//        Customer oldCustomer = Customer.builder()
//                .id(id)
//                .name("Old Name")
//                .email("oldEmail@gmail.com")
//                .age(25)
//                .build();
//
//        given(customerDao.getCustomerById(any(Long.class))).willReturn(Optional.of(oldCustomer));
//
//        //When
//        if (request.email() == null) {
//            underTest.updateCustomer(id, request);
//            then(customerDao).should(never()).existsByEmail(any(String.class));
//        } else {
//            given(customerDao.existsByEmail(any(String.class))).willReturn(false);
//            underTest.updateCustomer(id, request);
//            then(customerDao).should().existsByEmail(request.email());
//        }
//
//        //Then
//        then(customerDao).should().getCustomerById(id);
//        if (request.email() != null)
//            then(customerDao).should().existsByEmail(request.email());
//        else
//            then(customerDao).should(never()).existsByEmail(any(String.class));
//        then(customerDao).should().updateCustomer(customerArgumentCaptor.capture());
//        Customer actual = customerArgumentCaptor.getValue();
//        assertThat(actual.getName()).isEqualTo(expectedName);
//        assertThat(actual.getEmail()).isEqualTo(expectedEmail);
//        assertThat(actual.getAge()).isEqualTo(expectedAge);
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("provideIncorrectCustomerRequest")
//    void itShouldThrowWhenUpdateCustomer(CustomerRequest request, RuntimeException expectedException) {
//        //Given
//        Long id = 10L;
//        Customer oldCustomer = Customer.builder()
//                .id(id)
//                .name("Old Name")
//                .email("oldEmail@gmail.com")
//                .age(25)
//                .build();
//
//        given(customerDao.getCustomerById(any(Long.class))).willReturn(Optional.of(oldCustomer));
//
//        if (request.email() != null && request.email().equals("takenEmail@gmail.com")) {
//            given(customerDao.existsByEmail(any(String.class))).willReturn(true);
//        } else if (request.email() != null && !request.email().equals(oldCustomer.getEmail())) {
//            given(customerDao.existsByEmail(any(String.class))).willReturn(false);
//        }
//
//
//        //When
//        //Then
//        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
//                .isInstanceOf(expectedException.getClass())
//                .hasMessageContaining(expectedException.getMessage());
//        then(customerDao).should().getCustomerById(id);
//        if (request.email() != null && !request.email().equals(oldCustomer.getEmail())) {
//            then(customerDao).should().existsByEmail(request.email());
//        } else {
//            then(customerDao).should(never()).existsByEmail(any(String.class));
//        }
//        then(customerDao).should(never()).updateCustomer(any(Customer.class));
//        then(customerDao).shouldHaveNoMoreInteractions();
//    }
//
//    static Stream<Arguments> provideIncorrectCustomerRequest() {
//        return Stream.of(
//                arguments(new CustomerRequest(null, null, null, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest("Old Name", "oldEmail@gmail.com", 25, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest(null, "oldEmail@gmail.com", null, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest(null, null, 25, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest("Old Name", null, null, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest(null, "oldEmail@gmail.com", 25, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest("Old Name", null, 25, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest("Old Name", "oldEmail@gmail.com", null, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest("Old Name", "oldEmail@gmail.com", 25, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest(null, "oldEmail@gmail.com", null, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest(null, "oldEmail@gmail.com", 25, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest("Old Name", "oldEmail@gmail.com", null, "password"), new RequestValidationException("No changes provided")),
//                arguments(new CustomerRequest("Old Name", "takenEmail@gmail.com", 25, "password"), new DuplicateResourceException("Email already taken")),
//                arguments(new CustomerRequest(null, "takenEmail@gmail.com", null, "password"), new DuplicateResourceException("Email already taken")),
//                arguments(new CustomerRequest(null, "takenEmail@gmail.com", 25, "password"), new DuplicateResourceException("Email already taken")),
//                arguments(new CustomerRequest("Old Name", "takenEmail@gmail.com", null, "password"), new DuplicateResourceException("Email already taken"))
//        );
//    }


}