
package edu.klu.it.studentrecord.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
	@GetMapping("/")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String doLogin(@RequestParam String username, @RequestParam String password, Model model) {
		if (username.equals("admin") && password.equals("admin"))
			return "redirect:/students";
		model.addAttribute("error", true);
		model.addAttribute("showUserInfo", false);
		return "login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}
}
