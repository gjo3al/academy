package com.wei.repository;

public interface RepositoryInterface<T> {
	default T create(T domainObject) { return null; }
	default T read(int id) { return null; }
	default T read(String username) { return null; }
	default T update(T domainObject) { return null; }
	default T delete(int id) { return null; }
	default T delete(String username) { return null; }
}
