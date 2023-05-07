package hotel.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;
@Data//tự động tạo các phương thức getter, setter, equals, hashCode và toString cho các thuộc tính của một lớp thông quan thư viện lombok
@Entity// đánh dấu lớp account là một thực thể 
@Table(name = "Account")//Account là tên của bảng trong cơ sở dữ liệu mà lớp account ánh xạ đến

public class Account {
	//thuộc tính id là khóa chính và được tự động sinh ra và tự động tăng bởi hqtcsdl 
	@Id
	@GeneratedValue
	private Long id;
	
	//thuộc tính username và password đều phải có ít nhất 5 kí tự
	@Size(min=5, message = "Tên đăng nhập ít nhất 5 kí tự")
	private String username;
	@Size(min=5, message = "Mật khẩu ít nhất 5 kí tự")
	private String password;
	
	private boolean active;//thuộc tính active để biết trạng thái tài khoản có hoạt động hay không
	private String roles;//thuộc tính roles để biết vai trò của tài khoản

	private Date createdAt;//thuôc tính createAt để biết thời gian tài khoản được tạo 
	
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
	private User user;//thuộc tính user là khóa ngoại 1 user có thể có nhiều tài khoản

	public void setup(Account account2) {
		this.id = account2.id;
		this.username = account2.username;
		this.password = account2.password;
		this.active = account2.active;
		this.roles = account2.roles;
		this.createdAt = account2.getCreatedAt();
		this.user = account2.getUser();
	}
	
	//thời gian tạo tài khoản sẽ được lấy bằng thời gian thực, và được thực hiện trước khi đưa vào csdl
	@PrePersist
	private void createdAt() {
		this.createdAt = new Date();
	}
}