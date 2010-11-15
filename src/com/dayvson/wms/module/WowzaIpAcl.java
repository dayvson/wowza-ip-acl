package com.dayvson.wms.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wowza.wms.amf.AMFDataList;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.IClient;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.request.RequestFunction;

public class WowzaIpAcl extends ModuleBase {

	private List<String> whitelist = new ArrayList<String>();
	private List<String> blacklist = new ArrayList<String>();
	public void onAppStart(IApplicationInstance appInstance) {
		WMSProperties props = appInstance.getProperties();
		if(props.containsKey("blacklist")){
			this.blacklist = new ArrayList<String>( Arrays.asList( props.getPropertyStr("blacklist").toLowerCase().split(",") ) );
		}
		if(props.containsKey("whitelist")){
			this.whitelist =new ArrayList<String>( Arrays.asList( props.getPropertyStr("whitelist").toLowerCase().split(",") ) );
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
	public Boolean clientIsWhitelisted(String clientIP)
	{
		Boolean isfree = false;
		if( this.checkHasIpInList(clientIP, this.whitelist)){
			isfree = true;
			getLogger().info("[WHITELIST] :: Accepted Connect, Client Bypass. IP: " + clientIP);
		}
		return isfree;
	}
	public Boolean clientIsBlacklisted(String clientIP)
	{
		Boolean isblock = false;
		if ( this.checkHasIpInList(clientIP, this.blacklist)){
			isblock = true;
			getLogger().info("[BLACKLIST] :: Client Rejected. IP: " + clientIP);
		}
		return isblock;
	}
	public Boolean checkHasIpInList(String clientIP, List<String> list){
		return list.contains(clientIP.trim());	
	}

	public List<String> getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
	}

	public List<String> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}

}