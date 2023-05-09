package hotel.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hotel.data.BookingRepository;
import hotel.model.Booking;

//import QLKS.Entity.Account;

@Controller
public class HomeController {
	@Autowired
	private BookingRepository bookingRepo;
	
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

	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.GET, value = "/viewReport")
	public String viewReport(@RequestParam(name = "startDate", required = false) String startDateStr,
	                          @RequestParam(name = "endDate", required = false) String endDateStr,
	                          Model model) {
	    if (StringUtils.isEmpty(startDateStr) || StringUtils.isEmpty(endDateStr)) {
	    	float total = 0;
	        List<Booking> bookings = (List<Booking>) bookingRepo.findAll();
	        for(Booking booking:bookings) {
	        	total+=booking.getTotalPrice();
	        }
	        model.addAttribute("total",total);
	        model.addAttribute("bookings", bookings);
	    } else {
	    	float total = 0;
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
	        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

	        // Lọc danh sách booking theo ngày bắt đầu và kết thúc
	        List<Booking> bookings = (List<Booking>) bookingRepo.findAll();
	        List<Booking> filteredBookings = filterAllByCheckBetween(bookings, startDate, endDate);
	        for(Booking booking:filteredBookings) {
	        	total+=booking.getTotalPrice();
	        }
	        model.addAttribute("total",total);
	        model.addAttribute("bookings", filteredBookings);
	        model.addAttribute("startDate", startDateStr);
	        model.addAttribute("endDate", endDateStr);
	    }if (!model.containsAttribute("startDate")) {
	        model.addAttribute("startDate", "");
	    }
	    if (!model.containsAttribute("endDate")) {
	        model.addAttribute("endDate", "");
	    }
	    return "viewReport";
	}

	// Lọc danh sách booking theo ngày bắt đầu và kết thúc
	private List<Booking> filterAllByCheckBetween(List<Booking> bookings, LocalDate startDate, LocalDate endDate) {
	    List<Booking> filteredBookings = new ArrayList<>();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    for (Booking booking : bookings) {
	        LocalDate checkin =  LocalDate.parse( booking.getCheckin(), formatter);
	        LocalDate checkout = LocalDate.parse( booking.getCheckout(), formatter);
	        if (checkin != null && checkout != null && !booking.isCancelled()) {
	            if (checkin.compareTo(startDate) >= 0 && checkout.compareTo(endDate) <= 0) {
	                filteredBookings.add(booking);
	            }
	        }
	    }
	    return filteredBookings;
	}

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
