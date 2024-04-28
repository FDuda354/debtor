package pl.dudios.debtor;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dudios.debtor.customer.controller.CustomerRequest;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.Role;
import pl.dudios.debtor.customer.repository.CustomerDao;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class MeinWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeinWebsiteApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerDao customerDao, PasswordEncoder passwordEncoder) {
        return args -> {

//            System.out.println("===========================================");
//            System.out.println("===========================================");
            for (int i = 0; i < 10; i++) {
                try {
                    Faker faker = new Faker();
                    Name name = faker.name();
                    Random random = new Random();
                    String firstName = name.firstName();
                    String lastName = name.lastName();
                    int age = random.nextInt(10, 99);
                    String uuid = UUID.randomUUID().toString().substring(0, 5);
                    CustomerRequest request = new CustomerRequest(firstName + " " + lastName, firstName + lastName + uuid + randomDomen(), age, "1111");
                    customerDao.insertCustomer(Customer.builder()
                            .name(request.name())
                            .email(request.email())
                            .age(request.age())
                            .role(Role.ROLE_USER)
                            .password(passwordEncoder.encode(request.password()))
                            .accountNonLocked(true)
                            .enabled(true)
                            .build());
//                System.out.println(
//                        "('" + firstName + " " + lastName + "', '" + firstName+lastName+uuid+"@gmail.com', " + passwordEncoder.encode("111")+", "+age + "),");
                } catch (Exception ignore) {
                    //ignore
                }
            }
//            System.out.println("===========================================");
//            System.out.println("===========================================");

        };
    }

    Random random = new Random();

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
