package com.dayvson.test.wms.module;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import socks.InetRange;

import com.dayvson.wms.module.WowzaIpAcl;
import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.request.RequestFunction;

/**
 * 
 * @author dayvson
 * @email dayvson@gmail.com
 * 
 */
@RunWith(JMock.class)
public class WowzaIpAclTest extends TestCase {
	private InetRange blacklist;
	private InetRange whitelist;
	
	private Mockery context = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	
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
	public void shouldPopulateBlackListWhenWowzaHasABlackList() {
		WowzaIpAcl w = new WowzaIpAcl();
		final String ips = "1.2.3.4,5.6.7.8";
		final IApplicationInstance appInstance = context.mock(IApplicationInstance.class);
		final WMSProperties prop = context.mock(WMSProperties.class);
		
		context.checking(new Expectations() {{
			oneOf(appInstance).getProperties(); will(returnValue(prop));
			oneOf(prop).containsKey("blacklist"); will(returnValue(true));
			oneOf(prop).containsKey("whitelist"); will(returnValue(false));
			oneOf(prop).getPropertyStr("blacklist"); will(returnValue(ips));
		}});
		
		w.onAppStart(appInstance);
		InetRange blackList = w.getBlacklist();
		
		assertTrue(blackList.contains("1.2.3.4"));
		assertTrue(blackList.contains("5.6.7.8"));
	}
	
	@Test
	public void shouldPopulateWhiteListWhenWowzaHasAWhiteList() {
		WowzaIpAcl w = new WowzaIpAcl();
		final String ips = "1.2.3.4,5.6.7.8";
		final IApplicationInstance appInstance = context.mock(IApplicationInstance.class);
		final WMSProperties prop = context.mock(WMSProperties.class);
		
		context.checking(new Expectations() {{
			oneOf(appInstance).getProperties(); will(returnValue(prop));
			oneOf(prop).containsKey("blacklist"); will(returnValue(false));
			oneOf(prop).containsKey("whitelist"); will(returnValue(true));
			oneOf(prop).getPropertyStr("whitelist"); will(returnValue(ips));
		}});
		
		w.onAppStart(appInstance);
		InetRange whiteList = w.getWhitelist();
		
		assertTrue(whiteList.contains("1.2.3.4"));
		assertTrue(whiteList.contains("5.6.7.8"));
	}
	
	
	@Test
	public void shouldNotPopulateBlackListWhenWowzaDoesNotHaveABlackList() {
		WowzaIpAcl w = new WowzaIpAcl();
		final IApplicationInstance appInstance = context.mock(IApplicationInstance.class);
		final WMSProperties prop = context.mock(WMSProperties.class);
		
		context.checking(new Expectations() {{
			oneOf(appInstance).getProperties(); will(returnValue(prop));
			oneOf(prop).containsKey("blacklist"); will(returnValue(false));
			oneOf(prop).containsKey("whitelist"); will(returnValue(false));
		}});
		
		w.onAppStart(appInstance);
		InetRange blackList = w.getBlacklist();
		
		assertTrue(blackList.getAll().length == 0);
	}
	
	@Test
	public void shouldNotPopulateWhiteListWhenWowzaDoesNotHaveAWhiteList() {
		WowzaIpAcl w = new WowzaIpAcl();
		final IApplicationInstance appInstance = context.mock(IApplicationInstance.class);
		final WMSProperties prop = context.mock(WMSProperties.class);
		
		context.checking(new Expectations() {{
			oneOf(appInstance).getProperties(); will(returnValue(prop));
			oneOf(prop).containsKey("blacklist"); will(returnValue(false));
			oneOf(prop).containsKey("whitelist"); will(returnValue(false));
		}});
		
		w.onAppStart(appInstance);
		InetRange whiteList = w.getWhitelist();
		
		assertTrue(whiteList.getAll().length == 0);
	}
	
	@Test
	public void shouldAcceptConnectionWhenClientIsOnTheWowzasWhiteList() {
		final IClient client = context.mock(IClient.class);
		final RequestFunction function = context.mock(RequestFunction.class);
		final AMFDataList params = context.mock(AMFDataList.class);
		final String clientIp = "127.0.0.1";
		
		context.checking(new Expectations() {{
			oneOf(client).getIp(); will(returnValue(clientIp));
			oneOf(client).acceptConnection();
		}});
		
		WowzaIpAcl w = new WowzaIpAcl();
		w.setWhitelist(whitelist);
		w.onConnect(client, function, params);
	}
	
	@Test
	public void shouldAcceptConnectionWhenClientIsNotOnTheWowzasWhiteListNorBlackList() {
		final IClient client = context.mock(IClient.class);
		final RequestFunction function = context.mock(RequestFunction.class);
		final AMFDataList params = context.mock(AMFDataList.class);
		final String clientIp = "6.6.6.6";
		
		context.checking(new Expectations() {{
			oneOf(client).getIp(); will(returnValue(clientIp));
			oneOf(client).acceptConnection();
		}});
		
		WowzaIpAcl w = new WowzaIpAcl();
		w.setWhitelist(whitelist);
		w.setBlacklist(blacklist);
		w.onConnect(client, function, params);
	}
	
	@Test
	public void shouldRejectConnectionWhenClientIsNotOnTheWowzasWhiteListAndItIsOnItsBlackList() {
		final IClient client = context.mock(IClient.class);
		final RequestFunction function = context.mock(RequestFunction.class);
		final AMFDataList params = context.mock(AMFDataList.class);
		final String clientIp = "127.127.127.1";
		
		context.checking(new Expectations() {{
			oneOf(client).getIp(); will(returnValue(clientIp));
			oneOf(client).rejectConnection("The Client IP was rejected because has in blacklist");
		}});
		
		WowzaIpAcl w = new WowzaIpAcl();
		w.setWhitelist(whitelist);
		w.setBlacklist(blacklist);
		w.onConnect(client, function, params);
	}
}
