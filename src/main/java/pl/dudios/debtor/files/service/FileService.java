package pl.dudios.debtor.files.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;
import pl.dudios.debtor.files.model.File;
import pl.dudios.debtor.files.repo.FileRepository;
import pl.dudios.debtor.utils.ExistingFileRenameUtils;
import pl.dudios.debtor.utils.SlugifyUtils;

import java.io.IOException;
import java.io.InputStream;

import static org.flywaydb.core.internal.util.StringUtils.getFileNameAndExtension;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ExistingFileRenameUtils renameUtils;

    public File uploadFile(MultipartFile cardImage) throws RequestValidationException {
        String newFileName = SlugifyUtils.slugifyFileName(cardImage.getOriginalFilename());
        newFileName = renameUtils.renameFileIfExists(newFileName);

        try (InputStream inputStream = cardImage.getInputStream()) {
            File image = File.builder()
                    .fileName(newFileName)
                    .content(inputStream.readAllBytes())
                    .type(getFileNameAndExtension(newFileName).getRight().toUpperCase())
                    .build();
            return fileRepository.save(image);
        } catch (IOException e) {
            log.error("Error while uploading image", e);
            throw new RequestValidationException("Cant save file");
        }
    }

    public Resource serveFiles(String fileName) {
        File creditCardImage = fileRepository.findById(fileName).orElseThrow(() -> {
            log.error("Cant Find file: {}", fileName);
            return new ResourceNotFoundException("Image not found");
        });
        return new ByteArrayResource(creditCardImage.getContent());
    }

    @Transactional
    public void deleteFile(String cardImage) {
        fileRepository.deleteById(cardImage);
    }
}

