package hotel.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import hotel.model.Account;
import hotel.model.Booking;
import hotel.model.Client;
import hotel.model.Room;
import hotel.data.BookingRepository;
import hotel.data.ClientRepository;
import hotel.data.RoomRepository;

@Controller // tiếp nhận và xử lý yêu cầu đặt phòng
@RequestMapping("/booking") // xử lý yêu cầu HTTP trên đường dẫn "/booking" mức class
//tạo thuộc tính "currenRoom" được lưu trữ trong session 
@SessionAttributes("currentRoom")
public class BookingController {

	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính
	private RoomRepository roomRepo;

	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private BookingRepository bookingRepo;

//	@ModelAttribute sử dụng để xác định một phương thức trả về đối tượng
//	Method room() sẽ trả về một đối tượng Room và được gán với tên "currentRoom" trong ModelAttribute
//	tạo dữ liệu đặt phòng
	@ModelAttribute("currentRoom")
	public Room room() {
		return new Room();
	}

//	đặt phòng theo id
	@GetMapping("/{id}") // xử lý yêu cầu HTTP trên đường dẫn "/booking/{id}"
	public String bookingForm(Model model, 
			// - lấy dữ liệu từ id và gán vào biến id
			@PathVariable("id") Long id, 
			// - lấy dữ liệu trong currentRoom và gán vào biến room
			@ModelAttribute("currentRoom") Room room) {
		Booking booking = new Booking();
		Room room2 = roomRepo.findById(id).orElse(null); // tìm phòng theo id trong csdl - orElse(null) nếu ko có thì = null
		if (room2 != null) { // nếu có phòng thì cập nhập cào biến room 
			room.setId(room2.getId());
			room.setName(room2.getName());
			room.setPrice(room2.getPrice());
			room.setType(room2.getType());
			room.setDescription(room2.getDescription());
			room.setImage(room2.getImage());
		}
//		đưa thông tin phòng và thông tin thuê phòng vào model 
		model.addAttribute("room", room2);
		model.addAttribute("booking", booking);
		return "bookingInfo"; // chuyển đến trang thông tin phòng thuê 
	}

	@PostMapping // xử lý data gửi đến trên đường dẫn "/booking" 
	public String createBooking(Model model, Booking currentBooking, HttpSession session) throws ParseException {
//		lấy thông tin phòng hiện tại từ session 
		Room room = (Room) session.getAttribute("currentRoom");
//		lấy thông tin tài khoản người dùng hiện tại từ session 
		Account account = (Account) session.getAttribute("currentAccount");
//		lấy thông tin ngày nhận và trả phòng trong currentBooking
		SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateReceipt = fomatter.parse(currentBooking.getCheckin());
		Date datePayment = fomatter.parse(currentBooking.getCheckout());
//		nếu ngày nhận mà lớn hơn hoặc bằng ngày trả thì 
//		trở lại trang thông tin thuê phòng để điền lại thông tin 
		if (dateReceipt.after(datePayment) || dateReceipt.equals(datePayment)) {
			model.addAttribute("message", "Kiem tra lai thoi gian");
			model.addAttribute("room", room);
			return "bookingInfo";
		}
//		tìm khách hàng trong csdl với tên người dùng 
		Client client = clientRepo.findByUser(account.getUser()).orElse(null);
//		cập nhập thông tin thuê phòng
		currentBooking.setRoom(room);
		currentBooking.setClient(client);
		// tính số milliseconds giữa ngày trả và ngày nhận
		long diff = datePayment.getTime() - dateReceipt.getTime(); 
		// chuyển đổi sang số ngày
		int totalDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS); 
		currentBooking.setTotalPrice(room.getPrice()*totalDays);
		currentBooking.setReceive(false);
		currentBooking.setCancelled(false);
		currentBooking.setPaid(false);
		bookingRepo.save(currentBooking);
		return "redirect:/room"; // chuyển đến phương thức viewList trong class roomController 
	}

	@GetMapping("/list") // xem danh sách các phòng đã đặt
	public String viewBookingList(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("currentAccount");
		Client client = clientRepo.findByUser(account.getUser()).orElse(null);
		List<Booking> bookings = filterByCancel(bookingRepo.findAllByClient(client));
//		lấy thông tin tài khoản rồi thông tin khách sau đó lấy ds phòng đã đặt
//		thêm vào model để chuyển đến cho trang bookingList.html
		model.addAttribute("bookings", bookings);
		return "bookingList";
	}

//	lấy danh sách các phòng đã đặt mà chưa hủy
	private List<Booking> filterByCancel(List<Booking> bookings) {
		List<Booking> list = new ArrayList<>();
		for (Booking booking : bookings) {
			if (booking.isCancelled() == false) {
				list.add(booking);
			}
		}
		return list;
	}

	@GetMapping("/cancel/{id}") // xử lý yêu cầu hủy phòng rồi về trang danh sách phòng đã thuê
	public String cancelBooking(@PathVariable("id") Long id) {
		Booking booking = bookingRepo.findById(id).orElse(null); // tìm phòng theo id trong csdl
		if (booking != null) {
			booking.setCancelled(true);
			bookingRepo.save(booking);
		}
		return "redirect:/booking/list";
	}
	
}
