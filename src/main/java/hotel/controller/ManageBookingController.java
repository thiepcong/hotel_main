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

@Controller
@RequestMapping("/manage/booking") 
@SessionAttributes("bookingRoom") 
public class ManageBookingController {

	@Autowired 
	private BookingRepository bookingRepo;

	@Autowired
	private RoomRepository roomRepo;

	@ModelAttribute("bookingRoom")
	private Room bookingRoom() {
		return new Room();
	}

//	tiếp nhận yêu cầu xem danh sách đặt phòng của các khách hàng 
	@GetMapping("/{id}") 
	private String viewBookingList(
			@PathVariable("id") Long id, Model model,
			@ModelAttribute("bookingRoom") Room bookingRoom) {
		Room room = roomRepo.findById(id).orElse(null);
		List<Booking> bookings = filterByCancel(bookingRepo.findAllByRoom(room));
		model.addAttribute("bookings", bookings);
		bookingRoom.setId(room.getId());
		return "manageBookingList"; 
	}

//	tìm những phòng được đặt bởi khách mà chưa bị hủy
	private List<Booking> filterByCancel(List<Booking> bookings) {
		List<Booking> list = new ArrayList<>();
		for (Booking booking : bookings) {
			if (booking.isCancelled() == false) { 
				list.add(booking);
			}
		}
		return list;
	}
	
//	tiếp nhận yêu cầu hủy đặt phòng của khách theo id
	@GetMapping("/cancel/{id}") // xử lý yêu cầu HTTP trên đường dẫn "/manage/cancel/{id}"
	public String cancelBooking(
			@PathVariable("id") Long id, 
			@SessionAttribute("bookingRoom") Room bookingRoom)
	{
		Booking booking = bookingRepo.findById(id).orElse(null);
		if (booking != null) { 
			booking.setCancelled(true);
			bookingRepo.save(booking);
		}
		Long maPhong = bookingRoom.getId();
		String link = "redirect:/manage/booking/" + maPhong.toString();
		return link;
	}
	
}
