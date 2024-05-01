package pl.dudios.debtor.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import pl.dudios.debtor.files.repo.FileRepository;

@Component
@RequiredArgsConstructor
public class ExistingFileRenameUtils {

    private final FileRepository fileRepository;

    public String renameFileIfExists(String fileName) {
        if (fileRepository.existsById(fileName)) {
            return renameAndCheck(fileName);
        }
        return fileName;
    }

    private String renameAndCheck(String fileName) {
        String newName = renameFileName(fileName);
        if (fileRepository.existsById(newName)) {
            newName = renameAndCheck(newName);
        }
        return newName;
    }

    private String renameFileName(String fileName) {
        String name = FilenameUtils.getBaseName(fileName);
        String[] split = name.split("-(?=[0-9]+$)");
        int counter = split.length > 1 ? Integer.parseInt(split[1]) + 1 : 1;
        return split[0] + "-" + counter + "." + FilenameUtils.getExtension(fileName);
    }
}
