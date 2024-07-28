package pl.dudios.debtor.storage.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleStorageService {

    private final Storage storage;

    @Value("${gcp.project-id}")
    private String bucketName;

    public void uploadFile(String key, MultipartFile file) throws IOException {
        BlobId blobId = BlobId.of(bucketName, key);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        Storage.BlobTargetOption precondition;
        if (storage.get(bucketName, key) == null) {
            precondition = Storage.BlobTargetOption.doesNotExist();
        } else {
            precondition = Storage.BlobTargetOption.generationMatch(storage.get(bucketName, key).getGeneration());
        }
        storage.create(blobInfo, file.getBytes(), precondition);
        log.info("File uploaded successfully");
    }

    public Blob downloadFile(String key) {
        BlobId blobId = BlobId.of(bucketName, key);
        return storage.get(blobId);

    }

    public void deleteFile(String key) {
        boolean deleted = storage.delete(BlobId.of(bucketName, key));
        if (deleted) {
            log.info("File {} deleted successfully", key);

        } else {
            log.warn("File {} could not be deleted", key);
        }
    }

}
