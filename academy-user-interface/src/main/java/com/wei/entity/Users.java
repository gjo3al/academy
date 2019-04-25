package com.wei.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class Users {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@NotNull
	@Size(min=1, message="is required")
	@Column(name="username")
	private String username;
	
	@NotNull
	@Size(min=1, message="is required")
	@Column(name="password")
	private String password;
	
	@NotNull
	@Column(name="enabled")
	private boolean enabled;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="detail_id")
	private UserDetail userDetail;

	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private List<Authorities> authorities;
	
	public Users() {
	}

	public Users(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	
	public List<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}
	
	public String getNickname() {
		return userDetail.getNickname();
	}
	
	public String getEmail() {
		return userDetail.getEmail();
	}

	public void addAuthority(String role) {
		if(authorities == null) 
			authorities = new ArrayList<>();
		Authorities authority = new Authorities(username, this);
		authorities.add(authority);
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled  + ", detail=" + userDetail + "]";
	}
	
}
