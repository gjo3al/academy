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
	
	private final static String USERNAME = "userForTest1";
	
	private final static String USER_EMAIL = "userForTest1@gmail.com";
	
	private final static String NEW_PASSWORD = "newPassword";
	
	private final static String TOKEN = "token";
	
	private final static int ID_OF_USER = 1;
	
	private final static String DUMMY_VALUE = "dummy";
	
	private final static int ID_NOT_EXIST = 0;
	
	@Before
	public void before() {
		injectTestUserWith(USERNAME, USER_EMAIL);
	}

	@Test
	public void read_exist() {
		
		given(userRepository.read(ID_OF_USER)).willReturn(user);
		
		assertThat(userService.read(ID_OF_USER), is(user));
	}
	
	@Test
	public void read_notExist() {
		
		given(userRepository.read(ID_NOT_EXIST)).willReturn(null);
		
		assertThat(userService.read(ID_NOT_EXIST), nullValue());
	}
	
	@Test
	public void create_success() {
		
		Users newUser = new Users(); 
		
		userService.create(newUser);
		
		then(userRepository).should().create(newUser);
	}
	
	@Test
	public void update_success() {
		
		user.setPassword(NEW_PASSWORD);
		
		userService.update(user);
		
		then(userRepository).should().update(user);
	}
	
	@Test
	public void findByUserName_exist() {
		
		given(userRepository.findByUserName(USERNAME)).willReturn(user);
		
		assertThat(userService.findByUserName(USERNAME), is(user));
	}
	
	@Test
	public void findByUserName_notExist() {
		
		given(userRepository.findByUserName(DUMMY_VALUE)).willReturn(null);
		
		assertThat(userService.findByUserName(DUMMY_VALUE), nullValue());
	}
	
	@Test
	public void findByEmail_exist() {
		
		given(userRepository.findByEmail(USER_EMAIL)).willReturn(user);
		
		assertThat(userService.findByEmail(USER_EMAIL), is(user));
	}
	
	@Test
	public void findByEmail_notExist() {
		
		given(userRepository.findByEmail(DUMMY_VALUE)).willReturn(null);
		
		assertThat(userService.findByEmail(DUMMY_VALUE), nullValue());
	}
	
	@Test
	public void passwordResetLink_success() {
		
		given(userRepository.findByUserName(USERNAME)).willReturn(user);
		
		given(userDetailRepository.read(ID_OF_USER)).
		willReturn(user.getUserDetail());
		
		given(generator.generateTempPassword(user))
			.willReturn(NEW_PASSWORD);
		
		given(generator.encode(NEW_PASSWORD))
			.willReturn(NEW_PASSWORD);
		
		given(generator.generateToken(user))
			.willReturn(TOKEN);
		
		userService.passwordResetLink(USERNAME);
		
		assertThat(user.getPassword(), is(NEW_PASSWORD));
		
		then(userRepository).should().update(user);
		
		then(emailService).should().passwordResetLink(
				USER_EMAIL, 
				TOKEN, 
				NEW_PASSWORD);
	}
	
	@Test
	public void verify_success() {
		
		given(userRepository.findByEmail(USER_EMAIL)).willReturn(user);
		
		given(generator.generateToken(user)).willReturn(TOKEN);
		
		assertThat(userService.verify(USER_EMAIL, TOKEN), is(user));
	}
	
	@Test
	public void verify_user_notExist() {
		
		given(userRepository.findByEmail(DUMMY_VALUE)).willReturn(null);
		
		assertThat(userService.verify(DUMMY_VALUE, anyString()), nullValue());
	}
	
	@Test
	public void verify_incorrectToken() {
		
		given(userRepository.findByEmail(USER_EMAIL)).willReturn(user);
		
		given(generator.generateToken(user)).willReturn(TOKEN);
		
		assertThat(userService.verify(USER_EMAIL, DUMMY_VALUE), nullValue());
	}
	
	@Test
	public void isUsernameAndEmailMatched_match() {
		
		given(userRepository.findByUserName(USERNAME)).willReturn(user);
		
		given(userDetailRepository.read(ID_OF_USER)).willReturn(user.getUserDetail());
		
		boolean actual = 
				userService.isUsernameAndEmailMatched(USERNAME, USER_EMAIL);
		
		assertTrue(actual);
	}
	
	@Test
	public void isUsernameAndEmailMatched_userNotExist() {
		
		given(userRepository.findByUserName(DUMMY_VALUE)).willReturn(null);
		
		boolean actual = 
				userService.isUsernameAndEmailMatched(DUMMY_VALUE, USER_EMAIL);
		
		assertFalse(actual);
	}
	
	@Test
	public void isUsernameAndEmailMatched_notMatch() {
		
		given(userRepository.findByUserName(USERNAME)).willReturn(user);
		
		given(userDetailRepository.read(ID_OF_USER)).willReturn(user.getUserDetail());
		
		boolean actual = 
				userService.isUsernameAndEmailMatched(USERNAME, DUMMY_VALUE);
		
		assertFalse(actual);
	}
	
	@Test
	public void resetPassword_success() {
		
		given(generator.encode(NEW_PASSWORD)).willReturn(NEW_PASSWORD);
		
		userService.resetPassword(user, NEW_PASSWORD);
		
		then(userRepository).should().update(user);
	}
	
	private void injectTestUserWith(String username, String email) {
		
		user = new Users();
		
		user.setId(ID_OF_USER);
		
		user.setUsername(USERNAME);
		
		user.setPassword(USERNAME);
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USER_EMAIL);
		
		detail.setNickname(USERNAME);
		
		detail.setUser(user);
		
		user.setUserDetail(detail);
	}
}
