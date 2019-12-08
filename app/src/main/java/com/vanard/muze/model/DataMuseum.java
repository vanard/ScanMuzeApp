package com.vanard.muze.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataMuseum{

	@SerializedName("data")
	private List<DataItem> data;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"DataMuseum{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}