package com.wei.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.wei.email.EmailService;
import com.wei.email.SecurityCodeGenerator;
import com.wei.entity.UserDetail;
import com.wei.entity.Users;
import com.wei.repository.UserDetailRepository;
import com.wei.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private UserDetailRepository userDetailRepository;
	
	@Mock
	private EmailService emailService;
	
	@Mock
	private SecurityCodeGenerator generator;
	
	@InjectMocks
	private UserService userService;
	
	@Captor
	ArgumentCaptor<Integer> intCaptor;
	
	@Captor
	ArgumentCaptor<Users> userCaptor;
	
	@Captor
	ArgumentCaptor<String> stringCaptor;
	
	private Users user;
	
	private final static String USERNAME_FOR_TEST = "userForTest1";
	
	private final static String USER_EMAIL_FOR_TEST = "userForTest1@gmail.com";
	
	private final static String NEW_PASSWORD_FOR_TEST = "newPassword";
	
	private final static String TOKEN_FOR_TEST = "token";
	
	private final static int USER_ID_FOR_TEST = 1;
	
	private final static String DUMMY_VALUE = "dummy";
	
	private final static int ID_NOTEXIST = 0;
	
	@Before
	public void before() {
		injectTestUserWith(USERNAME_FOR_TEST, USER_EMAIL_FOR_TEST);
	}

	@Test
	public void read_exist() {
		
		given(userRepository.read(USER_ID_FOR_TEST)).willReturn(user);
		
		assertThat(userService.read(USER_ID_FOR_TEST), is(user));
	}
	
	@Test
	public void read_not_exist() {
		
		given(userRepository.read(ID_NOTEXIST)).willReturn(null);
		
		assertThat(userService.read(ID_NOTEXIST), nullValue());
	}
	
	@Test
	public void create_success() {
		
		Users newUser = new Users(); 
		
		userService.create(newUser);
		
		then(userRepository).should().create(newUser);
	}
	
	@Test
	public void update() {
		
		user.setPassword(NEW_PASSWORD_FOR_TEST);
		
		userService.update(user);
		
		then(userRepository).should().update(user);
	}
	
	@Test
	public void findByUserName_exist() {
		
		given(userRepository.findByUserName(USERNAME_FOR_TEST)).willReturn(user);
		
		assertThat(userService.findByUserName(USERNAME_FOR_TEST), is(user));
	}
	
	@Test
	public void findByUserName_not_exist() {
		
		given(userRepository.findByUserName(DUMMY_VALUE)).willReturn(null);
		
		assertThat(userService.findByUserName(DUMMY_VALUE), nullValue());
	}
	
	@Test
	public void findByEmail_exist() {
		
		given(userRepository.findByEmail(USER_EMAIL_FOR_TEST)).willReturn(user);
		
		assertThat(userService.findByEmail(USER_EMAIL_FOR_TEST), is(user));
	}
	
	@Test
	public void findByEmail_not_exist() {
		
		given(userRepository.findByEmail(DUMMY_VALUE)).willReturn(null);
		
		assertThat(userService.findByEmail(DUMMY_VALUE), nullValue());
	}
	
	@Test
	public void passwordResetLink() {
		
		given(userRepository.findByUserName(USERNAME_FOR_TEST)).willReturn(user);
		
		given(userDetailRepository.read(USER_ID_FOR_TEST)).
		willReturn(user.getUserDetail());
		
		given(generator.generateTempPassword(user))
			.willReturn(NEW_PASSWORD_FOR_TEST);
		
		given(generator.encode(NEW_PASSWORD_FOR_TEST))
			.willReturn(NEW_PASSWORD_FOR_TEST);
		
		given(generator.generateToken(user))
			.willReturn(TOKEN_FOR_TEST);
		
		userService.passwordResetLink(USERNAME_FOR_TEST);
		
		assertThat(user.getPassword(), is(NEW_PASSWORD_FOR_TEST));
		
		then(userRepository).should().update(user);
		
		then(emailService).should().passwordResetLink(
				USER_EMAIL_FOR_TEST, 
				TOKEN_FOR_TEST, 
				NEW_PASSWORD_FOR_TEST);
	}
	
	@Test
	public void verify_success() {
		
		given(userRepository.findByEmail(USER_EMAIL_FOR_TEST)).willReturn(user);
		
		given(generator.generateToken(user)).willReturn(TOKEN_FOR_TEST);
		
		assertThat(userService.verify(USER_EMAIL_FOR_TEST, TOKEN_FOR_TEST), is(user));
	}
	
	@Test
	public void verify_user_not_exist() {
		
		given(userRepository.findByEmail(DUMMY_VALUE)).willReturn(null);
		
		assertThat(userService.verify(DUMMY_VALUE, anyString()), nullValue());
	}
	
	@Test
	public void verify_incorrect_token() {
		
		given(userRepository.findByEmail(USER_EMAIL_FOR_TEST)).willReturn(user);
		
		given(generator.generateToken(user)).willReturn(TOKEN_FOR_TEST);
		
		assertThat(userService.verify(USER_EMAIL_FOR_TEST, DUMMY_VALUE), nullValue());
	}
	
	@Test
	public void isUsernameAndEmailMatched_match() {
		
		given(userRepository.findByUserName(USERNAME_FOR_TEST)).willReturn(user);
		
		given(userDetailRepository.read(USER_ID_FOR_TEST)).willReturn(user.getUserDetail());
		
		boolean actual = 
				userService.isUsernameAndEmailMatched(USERNAME_FOR_TEST, USER_EMAIL_FOR_TEST);
		
		assertTrue(actual);
	}
	
	@Test
	public void isUsernameAndEmailMatched_user_not_exist() {
		
		given(userRepository.findByUserName(DUMMY_VALUE)).willReturn(null);
		
		boolean actual = 
				userService.isUsernameAndEmailMatched(DUMMY_VALUE, USER_EMAIL_FOR_TEST);
		
		assertFalse(actual);
	}
	
	@Test
	public void isUsernameAndEmailMatched_not_match() {
		
		given(userRepository.findByUserName(USERNAME_FOR_TEST)).willReturn(user);
		
		given(userDetailRepository.read(USER_ID_FOR_TEST)).willReturn(user.getUserDetail());
		
		boolean actual = 
				userService.isUsernameAndEmailMatched(USERNAME_FOR_TEST, DUMMY_VALUE);
		
		assertFalse(actual);
	}
	
	@Test
	public void resetPassword_success() {
		
		given(generator.encode(NEW_PASSWORD_FOR_TEST)).willReturn(NEW_PASSWORD_FOR_TEST);
		
		userService.resetPassword(user, NEW_PASSWORD_FOR_TEST);
		
		then(userRepository).should().update(user);
	}
	
	private void injectTestUserWith(String username, String email) {
		
		user = new Users();
		
		user.setId(USER_ID_FOR_TEST);
		
		user.setUsername(USERNAME_FOR_TEST);
		
		user.setPassword(USERNAME_FOR_TEST);
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USER_EMAIL_FOR_TEST);
		
		detail.setNickname(USERNAME_FOR_TEST);
		
		detail.setUser(user);
		
		user.setUserDetail(detail);
	}
}
