package pl.dudios.debtor.utils;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.dudios.debtor.customer.controller.CustomerRequest;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.Gender;
import pl.dudios.debtor.customer.model.Role;
import pl.dudios.debtor.customer.repository.CustomerRepo;
import pl.dudios.debtor.customer.service.CustomerService;
import pl.dudios.debtor.debt.controller.DebtRequest;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.service.DeptService;
import pl.dudios.debtor.transaction.controller.TransactionRequest;
import pl.dudios.debtor.transaction.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDB {

    private final TransactionService transactionService;
    private final CustomerRepo customerDao;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final DeptService deptService;


    Random random = new Random();


    public void init() {
//            try {
//                Faker faker = new Faker();
//                Gender gender = random.nextInt(1, 10) / 2 == 1 ? Gender.MALE : Gender.FEMALE;
//                Name name = faker.name();
//                Random random = new Random();
//                String firstName = name.firstName();
//                String lastName = name.lastName();
//                int age = random.nextInt(10, 99);
//                String uuid = UUID.randomUUID().toString().substring(0, 5);
//                CustomerRequest request = new CustomerRequest(firstName, lastName, firstName + lastName + uuid + randomDomen(), age, gender, "1111");
//                Customer customer1 = customerDao.insertCustomer(Customer.builder()
//                        .firstName(firstName)
//                        .surname(lastName)
//                        .email(request.email().toLowerCase())
//                        .age(request.age())
//                        .role(Role.ROLE_USER)
//                        .gender(gender)
//                        .password(passwordEncoder.encode(request.password()))
//                        .accountNonLocked(true)
//                        .enabled(true)
//                        .build());
//
//                gender = random.nextInt(1, 10) / 2 == 1 ? Gender.MALE : Gender.FEMALE;
//                name = faker.name();
//                random = new Random();
//                firstName = name.firstName();
//                lastName = name.lastName();
//                age = random.nextInt(10, 99);
//                uuid = UUID.randomUUID().toString().substring(0, 5);
//                request = new CustomerRequest(firstName, lastName, firstName + lastName + uuid + randomDomen(), age, gender, "1111");
//                Customer customer2 = customerDao.insertCustomer(Customer.builder()
//                        .firstName(firstName)
//                        .surname(lastName)
//                        .email(request.email().toLowerCase())
//                        .age(request.age())
//                        .gender(gender)
//                        .role(Role.ROLE_USER)
//                        .password(passwordEncoder.encode(request.password()))
//                        .accountNonLocked(true)
//                        .enabled(true)
//                        .build());
//
//                LocalDateTime time = LocalDateTime.now();
//                time.plusDays(random.nextInt(10));
//                time.plusMonths(random.nextInt(10));
//                time.plusYears(random.nextInt(10));
//                for (int k = 0; k < random.nextInt(20); k++) {
//                    DebtRequest debtRequest = new DebtRequest(customer2.getEmail().toLowerCase(), customer1.getEmail(), BigDecimal.valueOf(random.nextInt(100_000) + 30_000), faker.commerce().productName(), time);
//                    DebtRequest debtRequest2 = new DebtRequest(customer1.getEmail().toLowerCase(), customer2.getEmail(), BigDecimal.valueOf(random.nextInt(100_000) + 30_000), faker.commerce().productName(), time);
//
//                    Debt debt = deptService.addDebt(debtRequest);
//                    Debt debt2 = deptService.addDebt(debtRequest);
//
//                    for (int j = 0; j < random.nextInt(35); j++) {
//                        TransactionRequest request1 = new TransactionRequest(debt.getId(), BigDecimal.valueOf(random.nextInt(1000) + 10), "Spłata", null);
//                        transactionService.addTransaction(request1);
//                    }
//                    for (int j = 0; j < random.nextInt(35); j++) {
//                        TransactionRequest request1 = new TransactionRequest(debt2.getId(), BigDecimal.valueOf(random.nextInt(1000) + 10), "Spłata", null);
//                        transactionService.addTransaction(request1);
//                    }
//                }
//
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
    }


    private String randomDomen() {
        String[] DOMAINS = {
                "@gmail.com", "@yahoo.com", "@hotmail.com", "@aol.com", "@outlook.com",
                "@icloud.com", "@mail.com", "@yandex.com", "@yahoo.co.uk", "@live.com",
                "@iCloud.com", "@comcast.net", "@googlemail.com", "@yahoo.co.jp", "@inbox.com",
                "@wp.pl"
        };


        int index = random.nextInt(DOMAINS.length);
        return DOMAINS[index];

    }

}
