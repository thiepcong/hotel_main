package hotel.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails{
	//MyUserDetails  có thể bao gồm các thuộc tính và phương thức khác của UserDetails
	private static final long serialVersionUID = 1L;
	//kiểu số dùng để đảm bảo rằng khi lưu trữ đối tượng của lớp MyUserDetails xuống cơ sở dữ liệu,
	//các phiên bản của đối tượng đều có một mã số phiên bản duy nhất để có thể truy xuất và xử lý
	
	private String username;// tên tài khoản
	private String password;// mật khẩu
	private boolean active;// trạng thái hoạt đọng
	private List<GrantedAuthority> authorities;// danh sách các quyền được cấp
	
	//khởi tạo không tham số
	public MyUserDetails() {
		
	}
	
	//khởi tạo với tài khoản
	public MyUserDetails(Account account) {
		this.username = account.getUsername();
		this.password = account.getPassword();
		this.active = account.isActive();
		this.authorities = Arrays.stream(account.getRoles().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
	
	//phương thức sử dụng thuộc tính authorities để trả về danh sách các quyền
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	//Trả về mật khẩu
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	
	//Trả về tên
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	
	// kiểm tra tài khaorn có hết hạn hay không trả về true tức là tài khoản có thời hạn vĩnh viễn
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	//xác định tài khoản người dùng có bị khóa hay không trả về true tức là không bị khóa
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	
	//kiểm tra xem thông tin đăng nhập của người dùng có hết hạn hay không trả về true tức là có hạn vĩnh viễn
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	//kiểm tra tài khoản có đang hoạt động không
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return active;
	}

}

// MyUserDetail lưu thông tin chi tiết người dùng gồm 5 thuộc tính và có hàm khơi tạo có tham số là 1 tài khoản