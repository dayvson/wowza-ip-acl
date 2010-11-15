package com.dayvson.test.wms.module;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayvson.wms.module.WowzaIpAcl;


public class WowzaIpAclTest extends TestCase {
	private ArrayList<String> blacklist;
	private ArrayList<String> whitelist;
	@Before
	public void setUp() throws Exception {
		blacklist = new ArrayList<String>();
		blacklist.add("127.127.127.1");
		blacklist.add("10.10.10.10");
		blacklist.add("1.2.3.4");
		whitelist = new ArrayList<String>();
		whitelist.add("127.0.0.1");
	}
	@After
	public void tearDown() throws Exception {
		blacklist = null;
		whitelist = null;
	}
	@Test
	public final void testChecheckHasIpInList() {
		String clientIP = "127.0.0.1";

		WowzaIpAcl restIp = new WowzaIpAcl();
		assertEquals(Boolean.TRUE, restIp.checkHasIpInList(clientIP, whitelist));
		assertEquals(Boolean.FALSE, restIp.checkHasIpInList(clientIP, blacklist));
	}
	@Test
	public final void testClientIsBlacklisted() {
		WowzaIpAcl rest = new WowzaIpAcl();
		rest.setBlacklist(blacklist);
		assertEquals(new Boolean(true), rest.clientIsBlacklisted("127.127.127.1"));
		assertEquals(new Boolean(false), rest.clientIsBlacklisted("1.1.1.1"));	
	}
	@Test
	public final void testClientIsWhitelisted() {
		WowzaIpAcl rest = new WowzaIpAcl();
		rest.setWhitelist(whitelist);
		assertEquals(new Boolean(true), rest.clientIsWhitelisted("127.0.0.1"));
		assertEquals(new Boolean(false), rest.clientIsWhitelisted("1.1.1.1"));	
	}
	@Test
	public final void testGetSetBlacklist(){
		WowzaIpAcl wow = new WowzaIpAcl();
		ArrayList<String> black = new ArrayList<String>();
		black.add("item-blacklist");
		wow.setBlacklist(black);
		assertEquals(black.size(), wow.getBlacklist().size());
	}
	@Test
	public final void testGetSetWhitelist(){
		WowzaIpAcl wow = new WowzaIpAcl();
		ArrayList<String> white = new ArrayList<String>();
		white.add("item-whitelist");
		wow.setWhitelist(white);
		assertEquals(white.size(), wow.getWhitelist().size());
	}
}
