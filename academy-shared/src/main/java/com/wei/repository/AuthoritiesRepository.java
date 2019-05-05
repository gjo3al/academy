package com.wei.repository;

import com.wei.entity.Authorities;

public interface AuthoritiesRepository extends RepositoryInterface<Authorities> {
	
	public boolean hasAuthority(int userId, String authorityName);
	
}
