package hotel.controller;


import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import QLKS.Entity.Account;

@Controller
public class HomeController {
	@GetMapping("/") // tiếp nhận yêu cầu từ trang /
	public String home(Model model, HttpSession session) {
		// dùng model để lưu data từ session
		if (session.getAttribute("currentAccount") != null) {
			model.addAttribute("account", session.getAttribute("currentAccount"));
		}
		// tìm đến trang giao diện homepage.html
		return "home";
	}
//	@GetMapping("/")
//	public String home() {
//		return "homepage";
//	}

	@GetMapping("/login") // tiếp nhận yêu cầu từ trang /login
	public String login(HttpSession session) {
		if (session.getAttribute("currentAccount") != null) {
			return "logout";
		}
		return "login";
	}
//	@GetMapping("/login")
//	public String login() {
//		return "login";
//	}

	@GetMapping("/logout") // tiếp nhận yêu cầu từ trang /logout
	public String logout(HttpSession session) {
//		lấy data trong session với name là currentAccount
//		kiểu dữ liệu là Account 
//		Account account = (Account) session.getAttribute("currentAccount");
//		if (account != null) { // nếu tồn tại thì ghi lại vào log
//			log.info("Log out: " + account);
//		}
//		nếu không thì chạy đến trang logout
		return "logout";
	}
//	@GetMapping("/logout")
//	public String logout() {
//		return "logout";
//	}
	
}
