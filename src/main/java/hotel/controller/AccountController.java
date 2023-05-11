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
@RequestMapping("/account") 
@SessionAttributes({ "addedUser", "addedClient" })
public class AccountController {

	@Autowired // tự động tiêm các đối tượng để sử dụng các phương thức và thuộc tính 
	private AccountRepository accountRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ClientRepository clientRepo;

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
		if (session.getAttribute("currentAccount") != null) { 
			Account account = (Account) session.getAttribute("currentAccount");
			model.addAttribute("account", account); 
		}
		return "account";
	}

	@GetMapping("/signup") // xử lý yêu cầu HTTP trên đường dẫn "/account/signup"
	public String signup(Model model) {
		model.addAttribute("user", new User()); // thêm data User vào model, tên là user
		model.addAttribute("client", new Client());
		return "signup"; // đến trang signup.html
	}
	
	// sử dụng để nhận thông tin của trang đăng ký tài khoản
	@PostMapping("/signup") // nhận data từ đường dẫn "account/signup" 
	public String submitClientInfo(Model model, 
			@Valid User user,@Valid Client client, Errors errors,
			@SessionAttribute("addedUser") User addedUser,
			@SessionAttribute("addedClient") Client addedClient) {
		if(errors.hasErrors()) { 
			return "signup";
		}
		addedUser.setFullname(user.getFullname());
		addedUser.setIdCard(user.getIdCard());
		addedUser.setPhoneNumber(user.getPhoneNumber());
		addedUser.setAddress(user.getAddress());
		addedUser.setEmail(user.getEmail());
		addedUser.setNote(user.getNote());
		addedUser.setRole("Khách hàng");
		
		addedClient.setBankAccount(client.getBankAccount());
		addedClient.setClientNote(client.getClientNote());
		model.addAttribute("account", new Account());
		return "signupAccount"; 
	}
	//sử dụng để nhận thông tin đăng nhập
		@PostMapping("/create")
		public String registerAccount(@Valid Account account, Errors errors, HttpSession session) {
			if(errors.hasErrors()) { 
				return "signupAccount";
			}
			
			User addedUser = (User) session.getAttribute("addedUser");
			Client addedClient = (Client) session.getAttribute("addedClient");
			addedClient.setAddress(addedUser.getAddress());
			addedClient.setPhoneNumber(addedUser.getPhoneNumber());
			addedClient.setEmail(addedUser.getEmail());
			addedClient.setFullname(addedUser.getFullname());
			addedClient.setIdCard(addedUser.getIdCard());
			// lưu data vào csdl gồm user, account và client
			userRepo.save(addedUser);
			
			account.setActive(true);
			account.setRoles("ROLE_USER");		
			account.setUser(addedUser);
			accountRepo.save(account);
			
			addedClient.setUser(addedUser);
			clientRepo.save(addedClient);
			return "redirect:/";
		}
	
	

	// hiển thị danh sách tài khoản KH cho phép admin có quyền truy cập đến trang accountList
	@GetMapping("/list")
	public String viewAccounts(Model model) {
		List<Account> accounts = filterByRole("ROLE_ADMIN", (List<Account>) accountRepo.findAll());
		model.addAttribute("accounts", accounts);
		return "accountList";
	}

	// trả kết quả 1 danh sách các tài khoản KH cho hàm viewAccount
	private List<Account> filterByRole(String role, List<Account> accounts) {
		List<Account> list = new ArrayList<>();
		for (Account account : accounts) {
			if (!account.getRoles().equals(role)) { 
				list.add(account);
			}
		}
		return list;
	}

	//  2 hàm dưới là tắt và bật tài khoản người dùng thông qua id
	@GetMapping("/disable/{id}")
	public String disableAccount(@PathVariable("id") Long id) {
		Account account = accountRepo.findById(id).orElse(null);
		if (account.isActive()) {
			account.setActive(false);
			accountRepo.save(account);
		}
		return "redirect:/account/list"; 
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
