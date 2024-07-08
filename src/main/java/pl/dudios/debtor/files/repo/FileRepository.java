package pl.dudios.debtor.files.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.files.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM images i " +
            "WHERE i.file_name = :fileName ",
            nativeQuery = true)
    Boolean existsByFileName(String fileName);
}

