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

@Controller 
@RequestMapping("/manage/room")// xử lý yêu cầu HTTP trên đường dẫn "/manage/room" mức class
@SessionAttributes("alteredRoom") // thuộc tính "alteredRoom" - được lưu trữ trong phiên
public class ManageRoomController {

	@Autowired 
	private RoomRepository roomRepo;

	@ModelAttribute("alteredRoom")
	public Room room() {
		return new Room();
	}

	@GetMapping 
	public String manageRoomFrm(Model model) {
		List<Room> rooms = (List<Room>) roomRepo.findAll();
		model.addAttribute("rooms", rooms);
		return "manageRoomList"; 
	}
	
//	xem chi tiết phòng 
	@GetMapping("/details/{id}") 
	public String manageRoomDetails(Model model, 
			@PathVariable("id") Long id) {
		Room room = roomRepo.findById(id).orElse(null);
		model.addAttribute("room", room);
		return "manageRoomDetail"; 
	}

//	thay đổi thông tin phòng theo id
	@GetMapping("/change/{id}") 
	public String changeRoomInfo(Model model, 
			@PathVariable("id") Long id,
			@SessionAttribute("alteredRoom") Room alteredroom) {
		Room room = roomRepo.findById(id).orElse(null);
		if (room != null) {
			alteredroom.setId(room.getId());
			alteredroom.setName(room.getName());
		}
		model.addAttribute("room", room);
		return "changeRoomDetail";
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
		return "redirect:/manage/room"; 
	}
	// xoá phòng theo id
	@GetMapping("/delete/{id}")
	public String deleteRoom(@PathVariable("id") Long id) {
		roomRepo.deleteById(id);
		return "redirect:/manage/room";
	}
	
//	tiếp nhận yêu cầu thêm phòng
	@GetMapping("/add") 
	public String addRoom(Model model) {
		model.addAttribute("room", new Room());
		return "addRoom";
	}
	
//	tiếp nhận data từ đường dẫn "/manage/room/add"
	@PostMapping("/add")
	public String saveRoom(Room room) {
		roomRepo.save(room);
		return "redirect:/manage/room";
	}
	
}
