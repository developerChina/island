package com.eparking.callback;

import com.eparking.data.PlateResult_Ex;
import com.sun.jna.Callback;

public interface IOnGetDataEx2 extends Callback
{
	public void callback(PlateResult_Ex.ByReference plateResult,long dwUser);
}

