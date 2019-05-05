package com.wei.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.wei.validation.ValidEmail;

@SuppressWarnings("serial")
@Entity
@Table(name="user_detail")
@IdClass(Users.class)
public class UserDetail implements Serializable {

	@Id
	@OneToOne
	@JoinColumn(name="users_id")
	private Users user;
	
	@Column(name="nickname")
	private String nickname;
	
	@ValidEmail
	@Column(name="email")
	private String email;
	
	public UserDetail() {
	}
	
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "UserDetail [id=" + user.getId() + ", nickname=" + nickname + ", email=" + email + "]";
	}

}
