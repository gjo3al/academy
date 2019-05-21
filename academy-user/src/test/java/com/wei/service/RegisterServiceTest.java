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
	
	private final static String USERNAME_FOR_TEST_1 = "userForTest1";
	
	private final static String USEREMAIL_FOR_TEST_1 = "userForTest1@gmail.com";
	
	private final static String PASSWORD_FOR_TEST_1 = "newPassword";
	
	private final static String AUTHORITY_FOR_TEST_1 = "auth1";
	
	private final static String AUTHORITY_FOR_TEST_2 = "auth2";
	
	private final static String TOKEN_FOR_TEST_1 = "token";
	
	private final static int USERID_FOR_TEST_1 = 1;
	
	@Test
	public void register() {
		
		Users user = prepareTestUserWithoutAuths();
		
		List<String> registerAuthorites = new ArrayList<>();
		registerAuthorites.add(AUTHORITY_FOR_TEST_1);
		registerAuthorites.add(AUTHORITY_FOR_TEST_2);
		
		given(registerData.getUser()).willReturn(user);
		given(registerData.getAuthorities()).willReturn(registerAuthorites);
		
		given(generator.encode(PASSWORD_FOR_TEST_1)).willReturn(PASSWORD_FOR_TEST_1);
		given(generator.generateToken(user)).willReturn(TOKEN_FOR_TEST_1);
		
		given(userRepository.create(user)).willReturn(user);
		
		registerService.register(registerData);
		
		then(authoritiesRepository).should(times(3)).create(any());
		
		then(emailService).should().validationLink(USEREMAIL_FOR_TEST_1, TOKEN_FOR_TEST_1);
	}
	
	private Users prepareTestUserWithoutAuths() {
		
		Users user = new Users();
		
		user.setId(USERID_FOR_TEST_1);
		
		user.setUsername(USERNAME_FOR_TEST_1);
		
		user.setPassword(PASSWORD_FOR_TEST_1);;
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USEREMAIL_FOR_TEST_1);
		
		detail.setNickname(USERNAME_FOR_TEST_1);
		
		detail.setUser(user);
		
		user.setUserDetail(detail);
		
		return user;
	}

}
