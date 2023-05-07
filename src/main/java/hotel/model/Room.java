package hotel.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data//tự động tạo các phương thức getter, setter, equals, hashCode và toString cho các thuộc tính của một lớp thông quan thư viện lombok
@Entity// đánh dấu lớp Room là một thực thể
@Table(name = "Room")//Booking là tên của bảng trong cơ sở dữ liệu mà lớp room ánh xạ đến
public class Room {
	//thuộc tính id là khóa chính và được tự động sinh ra và tự động tăng bởi hqtcsdl
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String type;
	private Long price;
	private String description;
}
//Room để lưu thông tin các phòng gồm 5 thuộc tính