package pl.dudios.debtor;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeinWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeinWebsiteApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(InitDB initDB) {
//        return args -> {
//            initDB.init();
//
//        };
//    }


}
