package hotel.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.Client;
import hotel.model.User;


//ClientRepository được sử dụng trong việc tương tác với cơ sở dữ liệu để lấy và lưu thông tin khách hàng.
//kế thừa crudrepository thao tác CRUD (Create, Read, Update, Delete) với đối tượng Client.

//CrudRepository<Client, Long> ClientRepository sẽ hoạt động với các đối tượng Client và sử dụng kiểu dữ liệu Long 
//cho thuộc tính ID của Client.
@Repository
public interface ClientRepository extends CrudRepository<Client, Long>{

	//phương thức findByUser(User user) để tìm kiếm một đối tượng Client dựa trên đối tượng User
	//Optional<Client> để tránh trả về null khi không tìm thấy đối tượng Client tương ứng với User.
	Optional<Client> findByUser(User user);

}