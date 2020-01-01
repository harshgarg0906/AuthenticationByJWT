package com.example.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsecurityjwt.jwt.JwtUtil;
import com.example.springsecurityjwt.model.AuthenticationResponse;
import com.example.springsecurityjwt.model.ComingData;
import com.example.springsecurityjwt.service.MyUserDetailService;import com.sun.corba.se.impl.logging.OMGSystemException;

@RestController
public class DataController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	MyUserDetailService userService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> setData(@RequestBody ComingData comingData) throws Exception
	{
		System.out.println("in the authentication controller");
		try
		{
			System.out.println("in the authentication");
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(comingData.getUserName(),
							comingData.getPassword()));
		}catch(BadCredentialsException e)
		{
			throw new Exception("Incorrect username or password", e);
		}
		
		UserDetails userDetails=userService.loadUserByUsername(comingData.getUserName());
		
		System.out.println("authentication is succesful");
		

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		System.out.println("sending the request back");
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	
	}	
	@GetMapping("/hello")
	public String getData()
	{
		return "hello jwt is working fine";
	}
}
