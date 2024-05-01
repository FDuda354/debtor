package pl.dudios.debtor.files.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.files.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
}

