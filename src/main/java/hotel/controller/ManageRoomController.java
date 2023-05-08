package hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import hotel.model.Room;
import hotel.data.RoomRepository;

@Controller // điều khiển chức năng thêm sửa xóa phòng 
@RequestMapping("/manage/room")// xử lý yêu cầu HTTP trên đường dẫn "/manage/room" mức class
@SessionAttributes("alteredRoom") // thuộc tính "alteredRoom" - được lưu trữ trong phiên
public class ManageRoomController {

	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính 
	private RoomRepository roomRepo;

//	@ModelAttribute sử dụng để xác định một phương thức trả về đối tượng
//	Method room() sẽ trả về một đối tượng Room và được gán với tên "alteredRoom" trong ModelAttribute
	@ModelAttribute("alteredRoom")
	public Room room() {
		return new Room();
	}

	@GetMapping // xử lý yêu cầu HTTP trên đường dẫn "/manage/room"
	public String manageRoomFrm(Model model) {
		// lấy danh sách các phòng trong csdl rồi thêm vào model
		List<Room> rooms = (List<Room>) roomRepo.findAll();
		model.addAttribute("rooms", rooms);
		return "manageRoomList"; // chuyển đến trang danh sách các phòng 
	}
	
//	xem chi tiết phòng 
	@GetMapping("/details/{id}") // xử lý yêu cầu HTTP trên đường dẫn "/manage/room/details/{id}"
	public String manageRoomDetails(Model model, 
			@PathVariable("id") Long id) {
//		tìm phòng theo id trong csdl và thêm vào model
		Room room = roomRepo.findById(id).orElse(null);
		model.addAttribute("room", room);
		return "manageRoomDetails"; // chuyển đến trang xem cụ thể 1 phòng
	}

//	thay đổi thông tin phòng theo id
	@GetMapping("/change/{id}") // xử lý yêu cầu HTTP trên đường dẫn "/manage/room/change/{id}"
	public String changeRoomInfo(Model model, 
			@PathVariable("id") Long id,
			@SessionAttribute("alteredRoom") Room alteredroom) {
//		tìm phòng theo id trong csdl
		Room room = roomRepo.findById(id).orElse(null);
		if (room != null) { //nếu có phòng thì cập nhập id và tên phòng vào alteredroom trong session
			alteredroom.setId(room.getId());
			alteredroom.setName(room.getName());
		}
//		thêm danh sách các phòng vào model rồi chuyển đến trang thay đổi thông tin phòng
		model.addAttribute("room", room);
		return "changeRoomDetails";
	}

	@PostMapping("/change") // tiếp nhận data thay đổi phòng trong SessionAttribute("alteredRoom")
	public String confirmChange(Room room, 
			@SessionAttribute("alteredRoom") Room alteredroom) {
//		cập nhập các thông tin như giá, loại phòng rồi lưu vào csdl
		alteredroom.setPrice(room.getPrice());
		alteredroom.setType(room.getType());
		alteredroom.setDescription(room.getDescription());
		alteredroom.setImage(room.getImage());
		roomRepo.save(alteredroom);
		return "redirect:/manage/room"; // chuyển đến phương thức manageRoomFrm()
	}
	
//	tiếp nhận yêu cầu thêm phòng
	@GetMapping("/add") // xử lý yêu cầu HTTP trên đường dẫn "/manage/room/add" 
	public String addRoom(Model model) {
//		khởi tạo 1 class Room() rỗng rồi lưu vào model để chuyển đến cho view
		model.addAttribute("room", new Room());
		return "manageAddRoom";
	}
	
//	tiếp nhận data từ đường dẫn "/manage/room/add"
	@PostMapping("/add")
	public String saveRoom(Room room) { // lưu vào csdl rồi chuyển đến phương thức manageRoomFrm()
//		log.info("Room: " + room);
		roomRepo.save(room);
		return "redirect:/manage/room";
	}
	
}
