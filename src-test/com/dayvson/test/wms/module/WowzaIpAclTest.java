package com.dayvson.test.wms.module;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import socks.InetRange;

import com.dayvson.wms.module.WowzaIpAcl;

/**
 * 
 * @author dayvson
 * @email dayvson@gmail.com
 * 
 */
public class WowzaIpAclTest extends TestCase {
	private InetRange blacklist;
	private InetRange whitelist;
	@Before
	public void setUp() throws Exception {
		blacklist = new InetRange();
		blacklist.add("127.127.127.1");
		blacklist.add("10.10.10.10");
		blacklist.add("1.2.3.4");
		whitelist = new InetRange();
		whitelist.add("127.0.0.1");
	}
	@After
	public void tearDown() throws Exception {
		blacklist = null;
		whitelist = null;
	}
	@Test
	public final void testFindIp(){
		InetRange range = new InetRange();
		range.add("10.10.10.10");
		range.add("127.127.123.123");
		assertEquals(true , range.contains("127.127.123.123"));
		assertEquals(true , range.contains("10.10.10.10"));
	}
	@Test
	public final void testContainsRangeIp(){
		InetRange range = new InetRange();
		range.add("127.0.0.1 127.0.0.10");
		range.add("100.200.300.0:100.200.300.255");
		assertEquals(true , range.contains("127.0.0.1"));
		assertEquals(true , range.contains("127.0.0.7"));
		assertEquals(true , range.contains("127.0.0.10"));
		assertEquals(true , range.contains("100.200.300.155"));
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
		InetRange black = new InetRange();
		black.add("item-blacklist");
		wow.setBlacklist(black);
		assertEquals(black.getAll().length, wow.getBlacklist().getAll().length);
	}
	@Test
	public final void testGetSetWhitelist(){
		WowzaIpAcl wow = new WowzaIpAcl();
		InetRange white = new InetRange();
		white.add("item-whitelist");
		wow.setWhitelist(white);
		assertEquals(white.getAll().length, wow.getWhitelist().getAll().length);
	}
}
