package pl.dudios.debtor.customer.controller;

import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.customer.friends.model.Friendship;
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

    @GetMapping("/details")
    public ResponseEntity<?> getCustomerDetails(@AuthenticationPrincipal Customer customer) {
        CustomerDTO customerDTO = customerService.getCustomerDTOById(customer.getId());
        return ResponseEntity.ok().body(customerDTO);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRequest request) {
        Customer customer = customerService.addCustomer(request);
        String token = jwtUtil.issueToken(customer.getEmail(), customer.getId(), customer.getRole().name());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addOrUpdateProfileImage(@AuthenticationPrincipal Customer customer,
                                                     @RequestParam("image") MultipartFile image) {
        customerService.addProfileImage(customer.getId(), image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> serveImage(@AuthenticationPrincipal Customer customer,
                                             @RequestParam(value = "customerImage", required = false) String customerImage) {

        Blob blob = customerService.getProfileImage(customer.getProfileImage(), customerImage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(blob.getName()).build());
        headers.setContentType(MediaType.parseMediaType(blob.getContentType()));
        headers.setContentLength(blob.getSize());
        if (blob.getCacheControl() != null) {
            headers.setCacheControl(blob.getCacheControl());
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(blob.getContent());
    }

    @GetMapping("/friends")
    public ResponseEntity<Page<CustomerDTO>> getFriends(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                        @AuthenticationPrincipal Customer customer) {
        Page<CustomerDTO> friends = customerService.getFriends(customer.getId(), page, size);
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
