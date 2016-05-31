package com.wellsbi.utils.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import com.google.common.collect.Iterables;
import com.wellsbi.utils.Json;

/**
 * Cache wrapper around Redis
 * 
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class RedisCache implements Cache {
	
	private static final Logger log = LoggerFactory.getLogger(RedisCache.class);
	
	private JedisPool pool;
	
	public RedisCache (RedisConfig config) {
		this.pool = _pool (config);
	}
	
	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#key(java.lang.String, java.lang.String)
	 */
	@Override
	public byte[] key (String region, String k) {
		return new StringBuilder(region)
			.append(Cache.REGION_SEPARATOR).append(k).toString().getBytes();
	}
	
	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#put(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public void put (String region, String k, Object v) {
		put (region, k, v, 30 * 60);
	}

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#forever(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public void forever (String region, String k, Object v) {
		put (region, k, v, Cache.FOREVER);
	}

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#put(java.lang.String, java.lang.String, T, int)
	 */
	@Override
	public <T> void put (String region, String k, T v, int expire) {
//		log.info("Caching {} in region {}", k, region);
		Jedis redis = _cache();
		
		// serialize value
		try {
			if (expire < 0) {
				// infinite ttl
				redis.set (key(region, k), Json.bytes(v));// JsonWriter.objectToJson(v).getBytes());
			} else {
				redis.setex (key(region, k), expire, Json.bytes(v)); // JsonWriter.objectToJson(v).getBytes());
			}
		} finally {
			__cache (redis);
		}
	}

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#updateTTL(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateTTL(String region, String k) {
		return updateTTL(region, k, 30 * 60);
	}

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#updateTTL(java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean updateTTL(String region, String k, int expire) {
		long retval = 0;
		Jedis redis = _cache();
		byte[] key = key (region, k);
		try {
			if (expire > 0) {
				retval = redis.expire (key, expire);
				log.debug("{}: Updated TTL on key {}{}{} to {} seconds",
						retval == 0 ? "FAIL" : "Success", region, Cache.REGION_SEPARATOR, k, expire);
			} else {
				retval = redis.persist (key);
				log.debug("{}: Persisted key {}{}{} ({} expiration given)",
						retval == 0 ? "FAIL" : "Success", region, Cache.REGION_SEPARATOR, k, expire);
			}
		} finally {
			__cache (redis);
		}
		return retval != 0;
	} // updateTTL

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#getJson(java.lang.String, java.lang.String)
	 */
	@Override
	public String getJson(String region, String k) {
//		log.debug("Getting from cache: {}{}{}", region, Cache.REGION_SEPARATOR, k);
		Jedis redis = _cache();
		try {
			return new String(redis.get (key(region, k)));
		} finally {
			__cache (redis);
		}
	}

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#get(java.lang.String, java.lang.String)
	 */
//	@Override
//	public Object get (String region, String k) {
////		log.debug("Getting from cache: {}{}{}", region, Cache.REGION_SEPARATOR, k);
//		Jedis redis = _cache();
//		try {
//			return Json.hydrate(new String(redis.get (key(region, k), clazz)
//					//JsonReader.jsonToJava(
//					//		new String(redis.get (key(region, k)))
//					// );
//		} finally {
//			__cache (redis);
//		}
//	}

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#get(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T get (String region, String k, Class<T> clazz) {
		// log.debug("Getting from cache: {}{}{}", region, Cache.REGION_SEPARATOR, k);
		return _get (key(region, k), clazz);
	}
	
	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#getAll(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> List<T> getAll (String region, String part, Class<T> clazz) {
		Jedis redis = _cache();
		List<T> matches = new ArrayList<T>();
		try {
			List<String> keys = _keys (region, part, redis);
			if (CollectionUtils.isNotEmpty(keys)) {
				/* should have full key collection now */
				keys.forEach((k) -> { matches.add(_get(k.getBytes(), clazz)); });
			}
		} finally {
			__cache (redis);
		}
		return matches;
	}
	
	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#evict(java.lang.String, java.lang.String)
	 */
	@Override
	public void evict (String region, String k) {
		log.debug("Evicting from cache: {}{}{}", region, Cache.REGION_SEPARATOR, k);
		Jedis redis = _cache();
		try {
			redis.del (key(region, k));
		} finally {
			__cache (redis);
		}
	}

	
	@Override
	public int evictAll(String region, String part) {
		// log.debug("Evicting all from cache: {}{}{}", region, Cache.REGION_SEPARATOR, part);
		Jedis redis = _cache();
		int evicted = 0;
		try {
			List<String> keys = _keys (region, part, redis);
			evicted = keys.size();
			if (CollectionUtils.isNotEmpty(keys)) {
				redis.del (Iterables.toArray(keys, String.class));
				log.info("{} keys evicted.", keys.size());
			}
		} finally {
			__cache (redis);
		}
		return evicted;
	}

	/* (non-Javadoc)
	 * @see com.wellsbi.hub.cache.Cache#clearRegion(java.lang.String)
	 */
	@Override
	public int clearRegion (String region) {
		return evictAll (region, "");
	}


	// ------------- privates
	private JedisPool _pool (RedisConfig rc) {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle (rc.getMinIdle());
        poolConfig.setMaxIdle (rc.getMaxIdle());
        poolConfig.setMaxTotal (rc.getMaxTotal());
        
        return new JedisPool (poolConfig, rc.getHost(), rc.getPort(), Protocol.DEFAULT_TIMEOUT, rc.getPassword());
	}
	
	private Jedis _cache() { return pool.getResource(); }
	
	private void __cache(Jedis jedis) { pool.returnResourceObject (jedis); }
	
	private String _kpart (String region, String regex) {
		return new StringBuilder(region)
				.append(Cache.REGION_SEPARATOR)
				.append(regex)
				.append(Cache.WILDCARD)
				.toString();
				
	}

	private <T> T _get (byte[] k, Class<T> clazz) {
		Jedis redis = _cache();
		try {
			byte[] v = redis.get (k);
			if (v == null) return null;
			return Json.hydrate(new String(v), clazz); // clazz.cast(JsonReader.jsonToJava(new String(v)));
		} finally {
			__cache (redis);
		}
	}
	
	private List<String> _keys (String region, String part, Jedis redis) {
		List<String> keys = null;
		ScanParams p = new ScanParams();
		p.match (_kpart(region, part));
		String cursor = "0";
		
		/* do an initial scan */
		ScanResult<String> sr = redis.scan (cursor, p);
		keys = sr.getResult();
		if (!CollectionUtils.isEmpty(keys)) {
			cursor = sr.getStringCursor();
			while (!"0".equals(cursor))  {
				sr = redis.scan(cursor, p);
				keys.addAll(sr.getResult());
				cursor = sr.getStringCursor();
			}
		} /* else {
			log.info("No matching keys found for {}", _kp);
		} */
		return keys; 
	}

}
