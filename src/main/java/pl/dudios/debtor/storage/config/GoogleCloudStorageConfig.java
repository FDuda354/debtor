package pl.dudios.debtor.storage.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${gcp.credentials.location}")
    private Resource credentialsResource;

    @Value("${gcp.project-id}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        try (InputStream credentialsStream = credentialsResource.getInputStream()) {
            return StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setProjectId(projectId)
                    .build()
                    .getService();
        }
    }

}