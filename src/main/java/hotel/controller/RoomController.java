package hotel.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import hotel.model.Room;
import hotel.data.RoomRepository;

@Controller // DANH SÁCH CÁC PHÒNG
@RequestMapping("/room") // xử lý yêu cầu HTTP trên đường dẫn "/room" mức class
public class RoomController {
	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính
	private RoomRepository roomRepo;

	// xem danh sách các phòng
	@GetMapping // xử lý yêu cầu HTTP trên đường dẫn giống mức lớp
	public String viewList(Model model, HttpSession session) {
		// lấy danh sách phòng từ csdl rồi đưa vào biến rooms 
		List<Room> rooms = (List<Room>) roomRepo.findAll();
		// đưa danh sách phòng vào model để chuển đến view 
		model.addAttribute("rooms", rooms);
		return "roomList"; // đến trang roomList.html
	}

	// xem chi tiết 1 phòng sử dụng id 
	@GetMapping("/details/{id}") // 
	public String viewDetails(Model model, @PathVariable("id") Long id, HttpSession session) {
		// @PathVariable("id") Long id - lấy dữ liệu từ id và gán vào biến id
		Room room = roomRepo.findById(id).orElse(null); // lấy data phòng, đưa vào model 
		model.addAttribute("room", room);
		return "roomDetails"; // đến trang roomDetails.html 
	}
}
