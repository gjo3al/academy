package com.wei.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.wei.email.EmailService;
import com.wei.email.SecurityCodeGenerator;
import com.wei.entity.RegistrationUser;
import com.wei.entity.UserDetail;
import com.wei.entity.Users;
import com.wei.repository.AuthoritiesRepositoryImpl;
import com.wei.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class RegisterServiceTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AuthoritiesRepositoryImpl authoritiesRepository;

	@Mock
	private SecurityCodeGenerator generator;
	
	@Mock
	private EmailService emailService;
	
	@Mock
	RegistrationUser registerData;
	
	@InjectMocks
	private RegisterService registerService;
	
	@Captor
	private ArgumentCaptor<String> stringCaptor;
	
	private final static String USERNAME_1 = "userForTest1";
	
	private final static String USEREMAIL_1 = "userForTest1@gmail.com";
	
	private final static String PASSWORD_1 = "newPassword";
	
	private final static String AUTHORITY_1 = "auth1";
	
	private final static String AUTHORITY_2 = "auth2";
	
	private final static String TOKEN = "token";
	
	private final static int ID_OF_USER = 1;
	
	@Test
	public void register_success() {
		
		Users user = prepareTestUserWithoutAuths();
		
		List<String> registerAuthorites = new ArrayList<>();
		registerAuthorites.add(AUTHORITY_1);
		registerAuthorites.add(AUTHORITY_2);
		
		given(registerData.getUser()).willReturn(user);
		given(registerData.getAuthorities()).willReturn(registerAuthorites);
		
		given(generator.encode(PASSWORD_1)).willReturn(PASSWORD_1);
		given(generator.generateToken(user)).willReturn(TOKEN);
		
		given(userRepository.create(user)).willReturn(user);
		
		registerService.register(registerData);
		
		then(authoritiesRepository).should(times(3)).create(any());
		
		then(emailService).should().validationLink(USEREMAIL_1, TOKEN);
	}
	
	private Users prepareTestUserWithoutAuths() {
		
		Users user = new Users();
		
		user.setId(ID_OF_USER);
		
		user.setUsername(USERNAME_1);
		
		user.setPassword(PASSWORD_1);;
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USEREMAIL_1);
		
		detail.setNickname(USERNAME_1);
		
		detail.setUser(user);
		
		user.setUserDetail(detail);
		
		return user;
	}

}
