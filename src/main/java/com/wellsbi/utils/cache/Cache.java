package com.wellsbi.utils.cache;

import java.util.List;

public interface Cache {
	// consts
	public static final String  WILDCARD = "*";
	public static final String  REGION_SEPARATOR = ":";
	public static final Integer FOREVER = -1; // negative number for eternal caching 
	
	/**
	 * @param region
	 * @param k
	 * @return
	 */
	public abstract byte[] key(String region, String k);

	/**
	 * @param region
	 * @param k
	 * @param v
	 */
	public abstract void put(String region, String k, Object v);

	/**
	 * @param region
	 * @param k
	 * @param v
	 */
	public abstract void forever(String region, String k, Object v);

	/**
	 * @param region
	 * @param k
	 * @param v
	 * @param expire
	 */
	public abstract <T> void put(String region, String k, T v, int expire);

	/** Updates to the default timeout
	 * @return true if the key was updated; false if the key was not found or not updated
	 * */
	public abstract boolean updateTTL(String region, String k);

	/** Updates the specified item in the cache with a new, specified TTL. If non-positive,
	 * then make the key persistent. If 0, this basically removes the key.
	 *
	 * @return true if the key was updated; false if the key was not found or not updated
	 */
	/**
	 * @param region
	 * @param k
	 * @param expire
	 * @return
	 */
	public abstract boolean updateTTL(String region, String k, int expire); // updateTTL

	/**
	 * @param region
	 * @param k
	 * @return
	 */
	public abstract String getJson(String region, String k);

//	/**
//	 * @param region
//	 * @param k
//	 * @return
//	 */
//	public abstract Object get(String region, String k);

	/**
	 * @param region
	 * @param k
	 * @param clazz
	 * @return
	 */
	public abstract <T> T get(String region, String k, Class<T> clazz);

	/**
	 * @param region
	 * @param part
	 * @param clazz
	 * @return
	 */
	public abstract <T> List<T> getAll(String region, String part,
			Class<T> clazz);

	/**
	 * @param region
	 * @param k
	 */
	public abstract void evict(String region, String k);

	/**
	 * @param region
	 * @param k
	 */
	public abstract int evictAll(String region, String part);

	/**
	 * @param region
	 * @return
	 */
	public abstract int clearRegion(String region);

}