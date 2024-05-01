package pl.dudios.debtor.customer.images.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.customer.images.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}

