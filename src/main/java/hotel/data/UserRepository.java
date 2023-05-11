package hotel.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.*;


@Repository
public interface UserRepository extends CrudRepository<User, Long>{

}