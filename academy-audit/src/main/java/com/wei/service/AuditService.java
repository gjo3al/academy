package com.wei.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.entity.Audit;
import com.wei.entity.Users;
import com.wei.repository.AuditRepository;
import com.wei.repository.UserRepository;

@Service
@Transactional
public class AuditService {

	@Autowired
	private AuditRepository auditRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Audit create(Users user, String address) {
		
		Audit audit = new Audit();
		
		audit.setUser(user);
		
		audit.setAddress(address);
		
		audit.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		return auditRepository.create(audit);
	}
	
	public List<Audit> readAll(String username) {

		int userId = userRepository.findByUserName(username).getId();
		
		return auditRepository.readAll(userId);
	}

	public void deleteAll(String username) {

		int userId = userRepository.findByUserName(username).getId();
		
		auditRepository.deleteAll(userId);
	}

}
