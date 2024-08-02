package pl.dudios.debtor;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
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
