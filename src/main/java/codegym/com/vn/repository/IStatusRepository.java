package codegym.com.vn.repository;

import codegym.com.vn.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStatusRepository extends JpaRepository<Status, Long> {

}
