package com.dayvson.wms.module;

import socks.InetRange;

import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.request.RequestFunction;
/**
 * 
 * @author dayvson
 * @email dayvson@gmail.com
 * 
 */
public class WowzaIpAcl extends ModuleBase {

	private InetRange whitelist = new InetRange();
	private InetRange blacklist = new InetRange();

	public void onAppStart(IApplicationInstance appInstance) {
		WMSProperties props = appInstance.getProperties();
		if(props.containsKey("blacklist")){
			this.fillBlacklist(props.getPropertyStr("blacklist"));
		}
		if(props.containsKey("whitelist")){
			this.fillWhitelist(props.getPropertyStr("whitelist"));
		}
	}
	
	public void onConnect(IClient client, RequestFunction function, AMFDataList params) {
		try{
		String clientIP = client.getIp();
		if (this.clientIsWhitelisted(clientIP) || !this.clientIsBlacklisted(clientIP)){
			client.acceptConnection();
		}else{
			client.rejectConnection("The Client IP was rejected because has in blacklist");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean clientIsWhitelisted(String clientIP){
		Boolean isfree = false;
		if( this.checkHasIpInList(clientIP, this.whitelist)){
			isfree = true;
			getLogger().info("[WHITELIST] :: Accepted Connect, Client Bypass. IP: " + clientIP);
		}
		return isfree;
	}
	
	public Boolean clientIsBlacklisted(String clientIP){
		Boolean isblock = false;
		if ( this.checkHasIpInList(clientIP, this.blacklist)){
			isblock = true;
			getLogger().info("[BLACKLIST] :: Client Rejected. IP: " + clientIP);
		}
		return isblock;
	}
	
	public Boolean checkHasIpInList(String clientIP, InetRange inetRange){
		return inetRange.contains(clientIP.trim());	
	}

	public InetRange getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(InetRange whitelist) {
		this.whitelist = whitelist;
	}

	public InetRange getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(InetRange blacklist) {
		this.blacklist = blacklist;
	}
	
	private void fillWhitelist(String ips){
		String[] list = ips.toLowerCase().trim().split(",");
		for (String item : list) {
			this.whitelist.add(item);
		}
	}
	private void fillBlacklist(String ips){
		String[] list = ips.toLowerCase().trim().split(",");
		for (String item : list) {
			this.blacklist.add(item);
		}
	}

}