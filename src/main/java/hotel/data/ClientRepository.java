package hotel.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.Client;
import hotel.model.User;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long>{

	//phương thức findByUser(User user) để tìm kiếm một đối tượng Client dựa trên đối tượng User
	//Optional<Client> để tránh trả về null khi không tìm thấy đối tượng Client tương ứng với User.
	Optional<Client> findByUser(User user);

}