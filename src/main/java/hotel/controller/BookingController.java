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

@Controller 
@RequestMapping("/booking") 
@SessionAttributes("currentRoom")
public class BookingController {

	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính
	private RoomRepository roomRepo;

	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private BookingRepository bookingRepo;

	@ModelAttribute("currentRoom")
	public Room room() {
		return new Room();
	}

//	đặt phòng theo id
	@GetMapping("/{id}") // xử lý yêu cầu HTTP trên đường dẫn "/booking/{id}"
	public String bookingForm(Model model, 
			@PathVariable("id") Long id, 
			@ModelAttribute("currentRoom") Room room) {
		Booking booking = new Booking();
		Room room2 = roomRepo.findById(id).orElse(null); 
		if (room2 != null) { 
			room.setId(room2.getId());
			room.setName(room2.getName());
			room.setPrice(room2.getPrice());
			room.setType(room2.getType());
			room.setDescription(room2.getDescription());
			room.setImage(room2.getImage());
		}
		model.addAttribute("room", room2);
		model.addAttribute("booking", booking);
		return "bookingInfo"; 
	}

	@PostMapping // xử lý data gửi đến trên đường dẫn "/booking" 
	public String createBooking(Model model, Booking currentBooking, HttpSession session) throws ParseException {
//		lấy thông tin  từ session 
		Room room = (Room) session.getAttribute("currentRoom");
		Account account = (Account) session.getAttribute("currentAccount");
		SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateReceipt = fomatter.parse(currentBooking.getCheckin());
		Date datePayment = fomatter.parse(currentBooking.getCheckout());
//		kiểm tra ngày
		if (dateReceipt.after(datePayment) || dateReceipt.equals(datePayment)) {
			model.addAttribute("message", "Kiem tra lai thoi gian");
			model.addAttribute("room", room);
			return "bookingInfo";
		}
		Client client = clientRepo.findByUser(account.getUser()).orElse(null);
		currentBooking.setRoom(room);
		currentBooking.setClient(client);
		long diff = datePayment.getTime() - dateReceipt.getTime(); 
		// chuyển đổi sang số ngày
		int totalDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS); 
		currentBooking.setTotalPrice(room.getPrice()*totalDays);
		currentBooking.setReceive(false);
		currentBooking.setCancelled(false);
		currentBooking.setPaid(false);
		bookingRepo.save(currentBooking);
		return "redirect:/room";  
	}

	@GetMapping("/list") // xem danh sách các phòng đã đặt
	public String viewBookingList(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("currentAccount");
		Client client = clientRepo.findByUser(account.getUser()).orElse(null);
		List<Booking> bookings = filterByCancel(bookingRepo.findAllByClient(client));
//		lấy thông tin tài khoản rồi thông tin khách sau đó lấy ds phòng đã đặt
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
		Booking booking = bookingRepo.findById(id).orElse(null); 
		if (booking != null) {
			booking.setCancelled(true);
			bookingRepo.save(booking);
		}
		return "redirect:/booking/list";
	}
	
}
