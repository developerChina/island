package com.eparking.callback;

import com.sun.jna.Callback;

public interface IOnConnectStatus extends Callback
{
	public void callback(String chWTYIP, int nStatus,long dwUser);
}
