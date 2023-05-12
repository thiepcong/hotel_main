package hotel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hotel.model.Account;

public class UserDetail implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	private String username;// tên tài khoản
	private String password;// mật khẩu
	private boolean active;// trạng thái hoạt đọng
	private List<GrantedAuthority> authorities;// danh sách các quyền được cấp
	
	public UserDetail() {
		
	}
	
	public UserDetail(Account account) {
		this.username = account.getUsername();
		this.password = account.getPassword();
		this.active = account.isActive();
		this.authorities = Arrays.stream(account.getRoles().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

}
