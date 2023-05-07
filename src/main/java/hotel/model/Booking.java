package hotel.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data//tự động tạo các phương thức getter, setter, equals, hashCode và toString cho các thuộc tính của một lớp thông quan thư viện lombok
@Entity// đánh dấu lớp Booking là một thực thể
@Table(name = "Booking")//Booking là tên của bảng trong cơ sở dữ liệu mà lớp booking ánh xạ đến
public class Booking {
	//thuộc tính id là khóa chính và được tự động sinh ra và tự động tăng bởi hqtcsdl 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = Room.class, cascade = CascadeType.MERGE)
	private Room room;// thuộc tính room là khoá ngoại một lần đặt có thể đặt nhiều phòng
	
	@ManyToOne(targetEntity = Client.class, cascade = CascadeType.MERGE)
	private Client client;//thuộc tính client là khóa ngoại 1 khách hàng có thể đặt nhiều lần
	
	private String checkin;//thuộc tính đại diên ngày nhận phòng
	private String checkout;//ngày trả phòng
	private Long totalPrice;// tổng tiền
	private boolean isReceive;//thuộc tính đại diện cho trạng thái khách nhận phòng hay chưa
	private boolean isPaid;//thuộc tính đại diện cho trạng thái khách thanh toán hay chưa
	private boolean isCancelled;//thuộc tính đại diện cho trạng thái đơn đặt phòng bị hủy hay chưa
	private String note;// ghi chú khác về đơn phòng
}
////Booking để lưu thông tin đặt trước phòng gồm 10 thuộc tính