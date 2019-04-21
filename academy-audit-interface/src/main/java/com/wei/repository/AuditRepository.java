package com.wei.repository;

import java.util.List;

import com.wei.entity.Audit;

public interface AuditRepository extends RepositoryInterface<Audit> {
	public List<Audit> readAll(int userId);
	public void deleteAll(int userId);
}
