package com.dayvson.test.wms.module;

import junit.framework.TestCase;

import org.junit.Test;

import com.dayvson.wms.module.WowzaIpAcl;


public class WowzaIpAclTest extends TestCase {

	@Test
	public final void testChecheckHasIpInList() {
		String clientIP = "127.0.0.1";
		String[] blacklist = {"200.200.200.201", "127.0.0.1", "10.10.10.10"};
		String[] whitelist = {"10.10.10.10"};
		WowzaIpAcl restIp = new WowzaIpAcl();
		assertEquals(new Boolean(true), restIp.checkHasIpInList(clientIP, blacklist));
		assertEquals(new Boolean(false), restIp.checkHasIpInList(clientIP, whitelist));
	}
	@Test
	public final void testClientIsBlacklisted() {
		String[] blacklist = {"200.200.200.201", "127.0.0.1", "10.10.10.10"};
		WowzaIpAcl rest = new WowzaIpAcl();
		rest.setBlacklist(blacklist);
		assertEquals(new Boolean(true), rest.clientIsBlacklisted("127.0.0.1"));
		assertEquals(new Boolean(false), rest.clientIsBlacklisted("1.1.1.1"));	
	}
	@Test
	public final void testClientIsWhitelisted() {
		String[] whitelist = {"200.200.200.201", "127.0.0.1", "10.10.10.10"};
		WowzaIpAcl rest = new WowzaIpAcl();
		rest.setWhitelist(whitelist);
		assertEquals(new Boolean(true), rest.clientIsWhitelisted("127.0.0.1"));
		assertEquals(new Boolean(false), rest.clientIsWhitelisted("1.1.1.1"));	
	}
}
