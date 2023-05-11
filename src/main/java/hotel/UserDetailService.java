package hotel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hotel.data.AccountRepository;
import hotel.model.Account;
import hotel.UserDetail;
@Service // đánh dấu class là 1 service 
// UserDetailsService được sử dụng bởi Spring Security 
// để load thông tin người dùng khi họ đăng nhập.
public class UserDetailService implements UserDetailsService{
	
	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính 
	private AccountRepository accountRepo;

	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		// tìm kiếm tài khoản của người dùng dựa trên username
		// sử dụng Optional, ta có thể tránh được việc xử lý lỗi do giá trị null gây ra
		Optional<Account> account = accountRepo.findByUsername(username);
		// Nếu tìm thấy tài khoản, phương thức sẽ tạo một instance mới của MyUserDetails 
		// bằng cách sử dụng account đó và trả về nó.
		account.orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
		
		return account.map(UserDetail::new).get();
	}

}