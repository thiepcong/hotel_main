package hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import hotel.model.Account;
import hotel.data.AccountRepository;
import lombok.extern.slf4j.Slf4j;

@Controller // lấy thông tin tài khoản người dùng
@RequestMapping("/authen") // xử lý yêu cầu HTTP trên đường dẫn "/authen" mức class
@SessionAttributes("currentAccount") // tạo thuộc tính currentAccount trong session
public class AuthenController {

	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính
	private AccountRepository accountRepo;

	@ModelAttribute("currentAccount") // tạo 1 lớp Account rỗng rồi cho vào model
	public Account account() {
		return new Account();
	}

	@GetMapping
	public String getUser(@ModelAttribute("currentAccount") Account currentAccount) {

		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		// tìm tài khoản có tên currentUsername trong csdl
		Account account = accountRepo.findByUsername(currentUsername).orElse(null);
		if (account != null) { // nếu có thì thêm vào model với tên là currentAccount
			currentAccount.setup(account);
		}
		return "redirect:/"; // chuyển đến hàm getMapping("/")
	}

}
