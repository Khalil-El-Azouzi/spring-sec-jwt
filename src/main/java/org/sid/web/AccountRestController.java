package org.sid.web;

import org.sid.entities.AppUser;
import org.sid.entities.RegidterForm;
import org.sid.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class AccountRestController {
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping("/register")
	public AppUser register(@RequestBody RegidterForm userForm) {
		if(!userForm.getPassword().equals(userForm.getRepassword()))
			throw new RuntimeException("Confirme your password !");
		AppUser userexist =  accountService.findUserByUsername(userForm.getUsername());
		if(userexist!=null) throw new RuntimeException("This user already exists !");
		AppUser newuser = new AppUser();
		newuser.setUsername(userForm.getUsername());
		newuser.setPassword(userForm.getPassword());
		accountService.saveUser(newuser);
		accountService.addRoleToUser(userForm.getUsername(), "USER");
		return newuser;
	}

}
