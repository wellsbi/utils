/**
 * 
 */
package com.wellsbi.utils.cache;

/**
 * Loads a redis configuration from the application config file and makes it available
 * via the app config mechanism. Will allow Guice to inject the Jedis pool wherever 
 * needed.
 *
 *
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class RedisConfig {
	String host;
	Integer port;
	Integer minIdle;
	Integer maxIdle;
	Integer maxTotal;
	String password;
	

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RedisConfig [host=" + host + ", port=" + port + ", minIdle="
				+ minIdle + ", maxIdle=" + maxIdle + ", maxTotal=" + maxTotal
				+ ", password=" + password + "]";
	}
	
	

}
