package hotel.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Booking")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = Room.class, cascade = CascadeType.MERGE)
	private Room room;// thuộc tính room là khoá ngoại một lần đặt có thể đặt nhiều phòng
	
	@ManyToOne(targetEntity = Client.class, cascade = CascadeType.MERGE)
	private Client client;//thuộc tính client là khóa ngoại 1 khách hàng có thể đặt nhiều lần
	
	private String checkin;
	private String checkout;
	private Long totalPrice;
	private boolean isReceive;//thuộc tính đại diện cho trạng thái khách nhận phòng hay chưa
	private boolean isPaid;//thuộc tính đại diện cho trạng thái khách thanh toán hay chưa
	private boolean isCancelled;//thuộc tính đại diện cho trạng thái đơn đặt phòng bị hủy hay chưa
	private String note;
}