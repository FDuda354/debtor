package pl.dudios.debtor.customer.images.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.customer.images.model.Image;
import pl.dudios.debtor.customer.images.repo.ImageRepository;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;
import pl.dudios.debtor.utils.ExistingFileRenameUtils;
import pl.dudios.debtor.utils.SlugifyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static org.flywaydb.core.internal.util.StringUtils.getFileNameAndExtension;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ExistingFileRenameUtils renameUtils;

    @CacheEvict(value = "images", key = "#newFileName")
    public Image uploadImageInternal(MultipartFile profileImage, String newFileName) throws RequestValidationException {
        try (InputStream inputStream = profileImage.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            Image image = Image.builder()
                    .fileName(newFileName)
                    .content(bytes)
                    .uploadDate(LocalDateTime.now())
                    .size((long) bytes.length)
                    .type(getFileNameAndExtension(newFileName).getRight().toUpperCase())
                    .build();
            return imageRepository.save(image);
        } catch (IOException e) {
            log.error("Error while uploading image", e);
            throw new RequestValidationException("Cant save file");
        }
    }


    public Image uploadImage(MultipartFile profileImage) throws RequestValidationException {
        String newFileName = SlugifyUtils.slugifyFileName(profileImage.getOriginalFilename());
        newFileName = renameUtils.renameFileIfExists(newFileName);
        return uploadImageInternal(profileImage, newFileName);
    }

    @Cacheable("images")
    public Resource serveFiles(String fileName) {
        Image creditCardImage = imageRepository.findById(fileName).orElseThrow(() -> {
            log.error("Cant Find file: {}", fileName);
            return new ResourceNotFoundException("Image not found");
        });
        return new ByteArrayResource(creditCardImage.getContent());
    }

    public void deleteImage(String cardImage) {
        imageRepository.deleteById(cardImage);
    }
}

