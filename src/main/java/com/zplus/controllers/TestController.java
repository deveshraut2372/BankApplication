package com.zplus.controllers;

import com.zplus.models.Items;
import com.zplus.models.User;
import com.zplus.repository.ItemsRepository;
import com.zplus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	@Autowired
	private UserService userService;

//	@GetMapping("/all")
//	public String allAccess() {
//		return "Public Content.";
//	}
	
//	@GetMapping("/user/{id}")
//	@PreAuthorize("hasRole('USER') or hasRole('AGENT') or hasRole('ADMIN')")
//	public String userAccess(@PathVariable("id") int id) {
//		return "User Content."+id ;
//	}

//	@GetMapping("/admin")
//	@PreAuthorize("hasRole('ADMIN')")
//	public String adminAccess() {
//		return "Admin Board.";
//	}

	@Autowired
	ItemsRepository itemsRepository;

	@PostMapping("/postapi")
	@PreAuthorize("hasRole('ADMIN')")
	public Items adminAccess(@RequestBody Items items) {
		System.out.println(items);
		System.out.println("I am in method");
		Items it=itemsRepository.save(items);
		return it;
	}

	@PutMapping("/updateapi")


	@GetMapping("/getapi")
	@PreAuthorize("hasRole('USER')")
	public List<Items> VENDORAccess() {
	//	System.out.println(items);
		System.out.println("I am in method");
		return itemsRepository.findAll();

	}
	@GetMapping("/getById/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getById(@PathVariable long id)
	{
		User user=userService.getById(id);
		return new ResponseEntity(user, HttpStatus.OK);
	}

// new create by devesh
	@GetMapping("/getAllUserByParentId/{parentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity getAllUserByParentId(@PathVariable("parentId") Integer parentId)
	{
		List list=userService.getAllUserByParentId(parentId);
		return new ResponseEntity(list,HttpStatus.OK);
	}

	@GetMapping("/getByUserId/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity getByUserId(@PathVariable Long id)
	{
		User user=userService.getByUserId(id);
		return new ResponseEntity(user, HttpStatus.OK);
	}



}
