package hotel.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hotel.model.*;

//UserRepository được sử dụng trong việc tương tác với cơ sở dữ liệu để lấy và lưu thông tin người dùng.
//kế thừa crudrepository thao tác CRUD (Create, Read, Update, Delete) với đối tượng User.

//CrudRepository<User, Long> UserRepository sẽ hoạt động với các đối tượng User và sử dụng kiểu dữ liệu Long 
//cho thuộc tính ID của User.
@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	//phương thức findByAccount(Account account) để tìm kiếm 1 đối tượng người dùng(user) dựa trên 1 tài khoản(account)
	//Optional<User> để tránh trả về null khi không tìm thấy đối tượng User tương ứng với account.
//	Optional<User> findByAccount(Account account);

	//phương thức findByUsername(String username) để tìm kiếm 1 tài khoản (account) dựa trên 1 tên người dùng(username)
	//Optional<Account> để tránh trả về null khi không tìm thấy đối tượng Account tương ứng với username.
//	Optional<Account> findByUsername(String username);

}