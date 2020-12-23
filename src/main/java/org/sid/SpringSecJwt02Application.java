package org.sid;

import java.util.stream.Stream;

import org.sid.dao.TaskRepository;
import org.sid.dao.UserRepository;
//import org.sid.dao.UserRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.sid.entities.Task;
import org.sid.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication//(exclude = { SecurityAutoConfiguration.class })
public class SpringSecJwt02Application implements CommandLineRunner {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	public AccountService accountService;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecJwt02Application.class, args);
	}
	
	@Bean
    public BCryptPasswordEncoder getPasswordEncoder() {//hachage de password
			return new BCryptPasswordEncoder();
           }
	

	@Override
	public void run(String... args) throws Exception {
		
		accountService.saveUser(new AppUser(null, "admin", "1234",null));
		accountService.saveUser(new AppUser(null, "user", "1234",null));
		accountService.saveRole(new AppRole(null, "ADMIN"));
		accountService.saveRole(new AppRole(null, "USER"));
		accountService.addRoleToUser("admin", "ADMIN");
		accountService.addRoleToUser("admin", "USER");
		accountService.addRoleToUser("user", "USER");

		Stream.of("T1","T2","T3").forEach(t->{
			taskRepository.save(new Task(null,t)); 
		});
		
		taskRepository.findAll().forEach(t->{
			System.out.println("ID Task : "+t.getId()+"\t"+"Name Task :"+t.getTaskName());
		});
		
		userRepository.findAll().forEach(u->{
		System.out.println("ID user : "+u.getId()+"\t"+
							"Name :"+u.getUsername()+"\t"+
							"Password  :"+u.getPassword()+"\t"+
							"Roles  :"+u.getRoles()+"\t");
	});
	

	}

}
