package hotel.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data//tự động tạo các phương thức getter, setter, equals, hashCode và toString cho các thuộc tính của một lớp thông quan thư viện lombok
@Entity// đánh dấu lớp User là một thực thể
@Table(name = "User")//User là tên của bảng trong cơ sở dữ liệu mà lớp user ánh xạ đến
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	//thuộc tính fullname idCard phonenumber đều không để giá trị null
	@NotBlank(message = "Họ tên không để trống")
	private String fullname;
	@NotBlank(message = "Mã số CCCD không để trống")
	private String idCard;
	@NotBlank(message = "SĐT không để trống")
	private String phoneNumber;	
	private String email;
	private String address;
	private String role;//thuộc tính role cho biết vai trò của người dùng
	private String note;
	private Date registeredAt;
	@PrePersist
	public void register() {
		this.registeredAt = new Date();
	}
}

