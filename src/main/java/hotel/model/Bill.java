package hotel.model;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data//tự động tạo các phương thức getter, setter, equals, hashCode và toString cho các thuộc tính của một lớp thông quan thư viện lombok
@Entity// đánh dấu lớp Bill là một thực thể
@Table(name = "Bill")//Bill là tên của bảng trong cơ sở dữ liệu mà lớp bill ánh xạ đến
public class Bill {
	//thuộc tính id là khóa chính và được tự động sinh ra và tự động tăng bởi hqtcsdl 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = Booking.class, cascade = CascadeType.MERGE)
	private Booking booking;//thuộc tính booking là khóa ngoại, 1 booking có thể có nhiều bill
	
	@ManyToOne(targetEntity = Account.class, cascade = CascadeType.MERGE)
	private Account receptionist;//thuộc tính account là khóa ngoại, 1 account có thể có nhiều bill
	
	private Date paymentDate; 
	private Long paymentAmount;// thuộc tính paymentAmuont là số tiền thanh toán
	private String paymentType;//thuộc tính paymentType là kiể thanh toán(tiền mặt hay chuyển khoản)
	
	//thời gian tạo bil sẽ được lấy bằng thời gian thực, và được thực hiện trước khi đưa vào csdl
	@PrePersist
	private void paidAt() {
		this.paymentDate = new Date();
	}
}
//Bill để lưu thông tin 1 hóa đơn đặt phòng gồm 6 thuộc tính