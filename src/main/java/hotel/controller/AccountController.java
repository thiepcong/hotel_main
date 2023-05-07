package hotel.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import hotel.model.Account;
import hotel.model.User;
import hotel.data.AccountRepository;
import hotel.data.ClientRepository;
import hotel.data.UserRepository;
import hotel.model.Client;


@Controller
@RequestMapping("/account") // xử lý yêu cầu HTTP trên đường dẫn "/account" mức class

//hai thuộc tính "addedUser" tức có tk và "addedClient" tức ko có tk được lưu trữ trong phiên
@SessionAttributes({ "addedUser", "addedClient" })
public class AccountController {

	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính 
	private AccountRepository accountRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ClientRepository clientRepo;
	
//	@ModelAttribute sử dụng để xác định một phương thức trả về đối tượng
//	Method addedUser() sẽ trả về một đối tượng User và được gán với tên "addedUser" trong ModelAttribute
	@ModelAttribute("addedUser") 
	public User addedUser() {
		return new User();
	}
	
	@ModelAttribute("addedClient")
	public Client addedClient() {
		return new Client();
	}
	
	@ModelAttribute("addedAccount")
	public Account addedAccount() {
		return new Account();
	}

	@GetMapping // xử lý yêu cầu HTTP trên đường dẫn "/account"
	public String account(Model model, HttpSession session) {
//		lấy data trong session với name là currentAccount
//   	kiểu dữ liệu là Account 
		if (session.getAttribute("currentAccount") != null) { // nếu tồn tại
			Account account = (Account) session.getAttribute("currentAccount");
			// lấy trong tài khoản User hiện tại rồi lưu vào model, đặt tên là account
			model.addAttribute("account", account); 
		}
		return "account"; // đến trang account.html
	}

	@GetMapping("/submitInfo") // xử lý yêu cầu HTTP trên đường dẫn "/account/submitInfo"
	public String submitInfo(Model model) {
		model.addAttribute("user", new User()); // thêm data User vào model, tên là user
		return "submitInfo"; // đến trang submitInfo.html
	}
	
	// sử dụng để nhận thông tin của trang đăng ký tài khoản
	@PostMapping("/submitInfo") // nhận data từ đường dẫn "account/submitInfo" 
	public String submitClientInfo(Model model, 
			@Valid User user, Errors errors, @SessionAttribute("addedUser") User addedUser) {
		if(errors.hasErrors()) { // nếu có lỗi thì quay lại "submitInfo.html"
			return "submitInfo";
		}
		// nếu không lỗi 
		// đưa data vào trong thuộc tính addedUser của session.
		addedUser.setFullname(user.getFullname());
		addedUser.setIdCard(user.getIdCard());
		addedUser.setPhoneNumber(user.getPhoneNumber());
		addedUser.setAddress(user.getAddress());
		addedUser.setEmail(user.getEmail());
		addedUser.setNote(user.getNote());
		addedUser.setRole("Khách hàng");
		
		model.addAttribute("client", new Client());
		return "submitClient"; // đến trang submitClient.html
	}
	
	//sử dụng để nhận thông tin thẻ ngân hàng
	@PostMapping("/submitClient") // nhận data từ đường dẫn "account/submitClient"
	public String createAccount(Model model, 
			@Valid Client client, Errors errors, 
			@SessionAttribute("addedClient") Client addedClient) {
		if(errors.hasErrors()) {
			return "submitClient"; // đến trang submitClient.html
		}
		
		addedClient.setBankAccount(client.getBankAccount());
		addedClient.setNote(client.getNote());
		model.addAttribute("account", new Account());
		return "createAccount"; // đến trang createAccount.html - trang đăng nhập
	}

	//sử dụng để nhận thông tin đăng nhập
	@PostMapping("/create")// nhận data từ đường dẫn "account/create"
	public String registerAccount(@Valid Account account, Errors errors, HttpSession session) {
		if(errors.hasErrors()) { // nếu có lỗi thì quay lại trang đăng nhập
			return "createAccount";
		}
		
		User addedUser = (User) session.getAttribute("addedUser");
		Client addedClient = (Client) session.getAttribute("addedClient");

		// lưu data vào csdl gồm user, account và client
		userRepo.save(addedUser);
		
		account.setActive(true);
		account.setRoles("ROLE_USER");		
		account.setUser(addedUser);
		accountRepo.save(account);
		
		addedClient.setUser(addedUser);
		clientRepo.save(addedClient);
		return "redirect:/"; // chuyển hướng đến hàm getMapping("/")
	}

	// hiển thị danh sách tài khoản KH cho phép admin có quyền truy cập đến trang accountList
	@GetMapping("/list")
	public String viewAccounts(Model model) {
		// accountRepo.findAll() tìm tất cả tài khoản trong csdl
		List<Account> accounts = filterByRole("ROLE_ADMIN", (List<Account>) accountRepo.findAll());
		// class account để lưu trữ thông tin tài khoản người dùng hiện tại
		model.addAttribute("accounts", accounts);
		return "accountList";
	}

	// trả kết quả 1 danh sách các tài khoản KH cho hàm viewAccount
	private List<Account> filterByRole(String role, List<Account> accounts) {
		List<Account> list = new ArrayList<>();
		for (Account account : accounts) {
			if (!account.getRoles().equals(role)) { // nếu ko phải tài khoản admin
				list.add(account);
			}
		}
		return list;
	}

	//  2 hàm dưới là tắt và bật tài khoản người dùng thông qua id
	@GetMapping("/disable/{id}")
	public String disableAccount(@PathVariable("id") Long id) {
		Account account = accountRepo.findById(id).orElse(null);// orElse(null) nếu ko có thì = null
		if (account.isActive()) {
			account.setActive(false);
			accountRepo.save(account);
		}
		return "redirect:/account/list"; // chuyển hướng đến hàm getMapping("/list")
	}

	@GetMapping("/enable/{id}")
	public String enableAccount(@PathVariable("id") Long id) {
		Account account = accountRepo.findById(id).orElse(null);
		if (!account.isActive()) {
			account.setActive(true);
			accountRepo.save(account);
		}
		return "redirect:/account/list";
	}
	@GetMapping("/delete/{id}")
	public String deleteAccount(@PathVariable("id") Long id) {
		accountRepo.deleteById(id);
		return "redirect:/account/list";
	}
	@GetMapping("/role/{id}")
	public String roleAccount(@PathVariable("id") Long id) {
		Account account = accountRepo.findById(id).orElse(null);
		if (account.getRoles().equals("ROLE_MANAGER")==true) {
			account.setRoles("ROLE_USER");
			accountRepo.save(account);
		}
		else {
			account.setRoles("ROLE_MANAGER");
			accountRepo.save(account);
		}
		return "redirect:/account/list";
	}

}
