package pl.dudios.debtor.storage.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCloudStorageConfig {

    @Bean
    public Storage storage() {
        return StorageOptions.newBuilder().setProjectId("dluznik").build().getService();
    }

}