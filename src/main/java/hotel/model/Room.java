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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String type;
	private Long price;
	private String image;
	private String description;
}