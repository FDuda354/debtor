package pl.dudios.debtor.customer.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.customer.images.service.ImageService;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.CustomerDTO;
import pl.dudios.debtor.customer.service.CustomerService;
import pl.dudios.debtor.friends.model.Friendship;
import pl.dudios.debtor.security.jwt.JwtUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;
    private final ImageService imageService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRequest request) {
        Customer customer = customerService.addCustomer(request);
        String token = jwtUtil.issueToken(customer.getEmail(), customer.getId(), customer.getRole().name());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }

    @PostMapping("/image")
    public ResponseEntity<?> addOrUpdateProfileImage(@AuthenticationPrincipal Customer customer,
                                                     @RequestParam("image") MultipartFile image) {
        customerService.addProfileImage(customer.getId(), image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/image/customer")
    public ResponseEntity<Resource> serveImage(@AuthenticationPrincipal Customer customer) throws IOException {
        Customer user = customerService.getCustomerById(customer.getId());
        Resource resource = imageService.serveFiles(user.getProfileImage());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(user.getProfileImage())))
                .body(resource);
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> serveImage(@RequestParam("customerImage") String customerImage) throws IOException {
        Resource resource = imageService.serveFiles(customerImage);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(customerImage)))
                .body(resource);
    }

    @GetMapping("/friends")
    public ResponseEntity<Page<CustomerDTO>> getAllFriends(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @AuthenticationPrincipal Customer customer) {
        Page<CustomerDTO> friends = customerService.getAllFriends(customer.getId(), page, size);
        return ResponseEntity.ok().body(friends);
    }

    @GetMapping("/friends/all")
    public ResponseEntity<List<CustomerDTO>> getAllFriends(@AuthenticationPrincipal Customer customer) {
        List<CustomerDTO> friends = customerService.getAllFriends(customer.getId());
        return ResponseEntity.ok().body(friends);
    }

    @PostMapping("/friend")
    public ResponseEntity<Friendship> addFriend(@AuthenticationPrincipal Customer customer,
                                                @RequestParam("email") String email) {
        Friendship friendship = customerService.addFriend(customer.getId(), email);
        return ResponseEntity.ok().body(friendship);
    }

    @DeleteMapping("/friend")
    public ResponseEntity<Void> deleteFriend(@AuthenticationPrincipal Customer customer,
                                             @RequestParam("friendId") Long friendId) {
        customerService.deleteFriend(customer.getId(), friendId);
        return ResponseEntity.noContent().build();
    }

}
