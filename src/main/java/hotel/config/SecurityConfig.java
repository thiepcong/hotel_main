package hotel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//bảo mật cho ứng dụng web.
@Configuration
@EnableWebSecurity // cho phép Spring Security sử dụng các cấu hình bảo mật
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính 
	UserDetailsService userDetailsService;
	
	//Phương thức configure(AuthenticationManagerBuilder auth) được sử dụng 
	// để cấu hình UserDetailsService để lấy thông tin người dùng
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// chỉ định UserDetailsService sẽ được sử dụng để truy vấn thông tin người dùng
		auth.userDetailsService(userDetailsService);
	}
	
	// cấu hình bảo mật cho các URL trên ứng dụng.
	protected void configure(HttpSecurity http) throws Exception {
		// http.authorizeRequests() chỉ định rằng yêu cầu sẽ được xác minh bằng cách nào  
		// Phương thức antMatchers chỉ định các URL và các quyền truy cập cần thiết để truy cập vào chúng.
		http.authorizeRequests()
		.antMatchers("/account/disable/**", "/account/enable/**", "/account/change/**").hasRole("ADMIN")
		.antMatchers("/manage/**").hasRole("MANAGER") // áp dụng các quy tắc bảo mật /manage/** với quyền manager 
		.antMatchers("/booking/**").hasAnyRole("USER")
		.antMatchers("/login", "/", "/account/**", "/room/**").permitAll()
		.anyRequest().authenticated();
		
		// http.formLogin() xác định cấu hình xác thực thông qua form đăng nhập.
		http.formLogin()
		.loginPage("/login") // chỉ định URL của trang đăng nhập. 
		.defaultSuccessUrl("/authen", true); // chỉ định URL được chuyển hướng đến khi đăng nhập thành công
		
		// đăng xuất của ứng dụng
		http.logout()
		.logoutUrl("/logout") // URL sẽ xử lý yêu cầu đăng xuất
		.logoutSuccessUrl("/") // URL sẽ được chuyển hướng đến khi đăng xuất thành công.
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID");
	}
	
	@Bean
	// mã hóa mật khẩu của người dùng trước khi lưu vào cơ sở dữ liệu
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
