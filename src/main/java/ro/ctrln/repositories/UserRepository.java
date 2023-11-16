package ro.ctrln.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.ctrln.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
