package pl.dudios.debtor.customer.service;

import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.customer.controller.CustomerRequest;
import pl.dudios.debtor.customer.friends.model.FriendShipStatus;
import pl.dudios.debtor.customer.friends.model.Friendship;
import pl.dudios.debtor.customer.friends.repository.FriendshipRepo;
import pl.dudios.debtor.customer.images.service.ImageService;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.CustomerDTO;
import pl.dudios.debtor.customer.model.Role;
import pl.dudios.debtor.customer.repository.CustomerRepo;
import pl.dudios.debtor.exception.DuplicateResourceException;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;
import pl.dudios.debtor.notification.model.Notification;
import pl.dudios.debtor.notification.service.NotificationService;
import pl.dudios.debtor.storage.service.GoogleStorageService;
import pl.dudios.debtor.utils.mappers.CustomerMapper;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final String IMAGE_RESOURCE = "profile-images";
    private final CustomerRepo customerRepo;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final FriendshipRepo friendshipRepo;
    private final NotificationService notificationService;
    private final GoogleStorageService googleStorageService;

    public Customer getCustomerById(final Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
    }

    public Customer getCustomerByEmail(final String email) {
        return customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email: " + email + " not found"));
    }

    public CustomerDTO getCustomerDTOById(final Long id) {
        return customerRepo.findById(id).map(CustomerMapper::mapToCustomerDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
    }

    public Customer addCustomer(CustomerRequest request) {

        if (customerRepo.existsByEmail(request.email().toLowerCase())) {
            throw new DuplicateResourceException("Email already taken");
        }
        return customerRepo.save(Customer.builder()
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

    public void updateCustomer(Long id, CustomerRequest request) {
        Customer oldCustomer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
        validateRequest(request, oldCustomer);

        customerRepo.save(oldCustomer);
    }

    private void validateRequest(CustomerRequest request, Customer oldCustomer) {

        boolean changed = false;

        if (request.email() != null && !request.email().isBlank() && !request.email().equalsIgnoreCase(oldCustomer.getEmail())) {
            if (customerRepo.existsByEmail(request.email().toLowerCase()) && !request.email().equalsIgnoreCase(oldCustomer.getEmail())) {
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


    @Transactional
    public Friendship addFriend(Long customerId, String email) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + customerId + " not found"));
        Customer newFriend = customerRepo.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email: " + email + " not found"));

        if (cantBeFriends(customer, newFriend)) {
            throw new RequestValidationException("cant Be Friends!");
        }

        Friendship friendship = Friendship.builder()
                .customer(customer)
                .friend(newFriend)
                .status(FriendShipStatus.ACCEPTED) //TODO Change as REQUESTED
                .build();

        notificationService.notifyUser(email, new Notification(customer.getEmail() + " dodał cię do znajomych"));
        return friendshipRepo.save(friendship);
    }

    private boolean cantBeFriends(Customer customer, Customer newFriend) {
        if (customer.getId().equals(newFriend.getId())) {
            log.error("Cannot add yourself as a friend");
            return true;
        }

        if (Boolean.TRUE.equals(friendshipRepo.friendshipExists(customer.getId(), newFriend.getId()))) {
            log.error("Friendship already exists");
            return true;
        }

        if (friendshipRepo.countFriendsByCustomerId(customer.getId()) > 400) {
            log.error("Friends list overload");
            return true;
        }

        return false;
    }

    public Page<CustomerDTO> getFriends(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Customer> friendsByCustomerId = friendshipRepo.findFriendsByCustomerId(customerId, pageable);
        return friendsByCustomerId.map(CustomerMapper::mapToCustomerDTO);
    }

    public void deleteFriend(Long customerId, Long friendId) {

        Friendship friendship = friendshipRepo.findByCustomerIdAndFriendId(customerId, friendId).orElseThrow(() -> new ResourceNotFoundException("Friendship with customerId: " + customerId + " and " + friendId + " not found"));
        friendshipRepo.deleteById(friendship.getId());
    }

    @Transactional
    public void addProfileImage(Long customerId, MultipartFile image) {
        Customer customer = getCustomerById(customerId);
        if (customer.getProfileImage() != null) {
            googleStorageService.deleteFile((IMAGE_RESOURCE + "/%s").formatted(customer.getProfileImage()));
        }
        String newImageId = UUID.randomUUID().toString();
        try {
            googleStorageService.uploadFile(
                    (IMAGE_RESOURCE + "/%s").formatted(newImageId),
                    image
            );
        } catch (IOException e) {
            log.error("Error while uploading profile image", e);
            throw new RuntimeException(e);
        }

        customer.setProfileImage(newImageId);
        customerRepo.save(customer);

    }

    public Blob getProfileImage2(String customerImage, String paramCustomerImage) {
        String finalImageId = Objects.nonNull(paramCustomerImage) ? paramCustomerImage : customerImage;

        if (Objects.nonNull(finalImageId)) {
            return googleStorageService.downloadFile((IMAGE_RESOURCE + "/%s").formatted(finalImageId));
        } else {
            log.error("no image name provided");
            throw new ResourceNotFoundException("no image name provided");
        }
    }
}
