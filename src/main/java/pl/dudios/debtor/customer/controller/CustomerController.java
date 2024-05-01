package pl.dudios.debtor.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.CustomerDTO;
import pl.dudios.debtor.customer.service.CustomerService;
import pl.dudios.debtor.security.jwt.JwtUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    @GetMapping()
    public Page<CustomerDTO> getCustomers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return customerService.getCustomers(page, size);
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable final Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRequest request) {
        Customer customer = customerService.addCustomer(request);
        String token = jwtUtil.issueToken(request.email(), customer.getId(), customer.getRole().name());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(token);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable final Long id) {
        customerService.deleteCustomerById(id);
    }

    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable final Long id,
                               @RequestBody CustomerRequest request) {
        customerService.updateCustomer(id, request);
    }
}
