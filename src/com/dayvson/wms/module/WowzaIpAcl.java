package com.dayvson.wms.module;

import com.wowza.wms.amf.*;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.client.*;
import com.wowza.wms.module.*;
import com.wowza.wms.request.*;

public class WowzaIpAcl extends ModuleBase {

	private String[] whitelist = {};
	private String[] blacklist = {};
	public void onAppStart(IApplicationInstance appInstance) {
		WMSProperties props = appInstance.getProperties();
		if(props.containsKey("blacklist")){
			this.blacklist = props.getPropertyStr("blacklist").toLowerCase().split(",");
		}
		if(props.containsKey("whitelist")){
			this.whitelist = props.getPropertyStr("whitelist").toLowerCase().split(",");
		}
	}

	public void onConnect(IClient client, RequestFunction function, AMFDataList params) {
		String clientIP = client.getIp();
		if (this.clientIsWhitelisted(clientIP) && !this.clientIsBlacklisted(clientIP)){
			client.acceptConnection();
		}else{
			client.rejectConnection("The Client IP was rejected because has in blacklist");
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
	public Boolean checkHasIpInList(String clientIP, String[] list){
		Boolean result = false;
		for (String item : list) {
			if(clientIP.equalsIgnoreCase(item.trim())){
				result = true;
			}
		}
		return result;
	}
	public String[] getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(String[] whitelist) {
		this.whitelist = whitelist;
	}

	public String[] getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(String[] blacklist) {
		this.blacklist = blacklist;
	}
}