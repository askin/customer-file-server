package ws.askin.files.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.askin.files.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {}
