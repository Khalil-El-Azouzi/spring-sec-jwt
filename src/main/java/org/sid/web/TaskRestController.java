package org.sid.web;

import java.util.List;
import java.util.Optional;

import org.sid.dao.TaskRepository;
import org.sid.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class TaskRestController {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@GetMapping("/tasks")
	public List<Task> getTasks() {
		return taskRepository.findAll();
		}
	
	@GetMapping("/tasks/{id}")
	public Optional<Task> getTask(@PathVariable("id") Long id) {
		return taskRepository.findById(id) ;
	}
	
	@PostMapping("/tasks")
	public Task save(@RequestBody Task t) {
		return taskRepository.save(t);
	}
	
	
	
}
