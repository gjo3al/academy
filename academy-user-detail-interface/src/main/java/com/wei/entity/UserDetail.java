package com.wei.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.wei.validation.ValidEmail;

@Entity
@Table(name="user_detail")
public class UserDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="nickname")
	private String nickname;
	
	@NotNull
	@ValidEmail
	@Column(name="email")
	private String email;

	public UserDetail() {

	}

	public UserDetail(int id, String nickname, String email) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
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
		return "UserDetail [id=" + id + ", nickname=" + nickname + ", email=" + email + "]";
	}

}
