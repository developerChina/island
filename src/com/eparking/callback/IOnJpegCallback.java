package com.eparking.callback;

import com.eparking.data.DevData_info;
import com.sun.jna.Callback;

public interface IOnJpegCallback extends Callback
{
	public void callback(DevData_info.ByReference JpegInfo,long dwUser);
}
