package hotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import hotel.model.Booking;
import hotel.model.Room;
import hotel.data.BookingRepository;
import hotel.data.RoomRepository;

@Controller // tiếp nhận những yêu cầu liên quan đến quản lý đặt phòng 
@RequestMapping("/manage/booking") // xử lý yêu cầu HTTP trên đường dẫn "/manage/booking" mức class
// thuộc tính "bookingRoom" - được lưu trữ trong phiên
@SessionAttributes("bookingRoom") 
public class ManageBookingController {

	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính 
	private BookingRepository bookingRepo;

	@Autowired
	private RoomRepository roomRepo;

//	@ModelAttribute sử dụng để xác định một phương thức trả về đối tượng
//	Method bookingRoom() sẽ trả về một đối tượng Room và được gán với tên "bookingRoom" trong ModelAttribute
	@ModelAttribute("bookingRoom")
	private Room bookingRoom() {
		return new Room();
	}

//	tiếp nhận yêu cầu xem danh sách đặt phòng của các khách hàng 
	@GetMapping("/{id}") // xử lý yêu cầu HTTP trên đường dẫn "/manage/booking/{id}"
	private String viewBookingList(
			@PathVariable("id") Long id, Model model,
			@ModelAttribute("bookingRoom") Room bookingRoom) {
//		tìm phòng theo id trong csdl
		Room room = roomRepo.findById(id).orElse(null);
//		lấy ra danh sách các phòng rồi cho vào model để hiện ra view
		List<Booking> bookings = filterByCancel(bookingRepo.findAllByRoom(room));
		model.addAttribute("bookings", bookings);
		bookingRoom.setId(room.getId());
		return "manageBookingList"; // chuyển đến trang quản lý đặt phòng 
	}

//	tìm những phòng được đặt bởi khách mà chưa bị hủy
	private List<Booking> filterByCancel(List<Booking> bookings) {
		List<Booking> list = new ArrayList<>();
		for (Booking booking : bookings) {
			if (booking.isCancelled() == false) { // nếu chưa bị hủy thì cho vào danh sách 
				list.add(booking);
			}
		}
		return list;
	}
	
//	tiếp nhận yêu cầu hủy đặt phòng của khách theo id
	@GetMapping("/cancel/{id}") // xử lý yêu cầu HTTP trên đường dẫn "/manage/cancel/{id}"
	public String cancelBooking(
			@PathVariable("id") Long id, // lấy dử liệu về id và gán vào biến id 
			@SessionAttribute("bookingRoom") Room bookingRoom) // lấy data trong session và gán vào cho bookingRoom
	{
//		tìm phong mà khách đã đặt theo id
		Booking booking = bookingRepo.findById(id).orElse(null);
		if (booking != null) { // nếu tồn tại thì hủy đặt phòng cho khách và lưu vào csdl 
			booking.setCancelled(true);
			bookingRepo.save(booking);
		}
//		lấy id phòng rồi quay lại phương thức viewBookingList
		Long maPhong = bookingRoom.getId();
		String link = "redirect:/manage/booking/" + maPhong.toString();
		return link;
	}
	
}
