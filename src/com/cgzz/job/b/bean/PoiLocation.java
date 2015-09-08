package com.cgzz.job.b.bean;

import com.amap.api.services.core.LatLonPoint;

public class PoiLocation {

	private String address;
	private LatLonPoint lat;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LatLonPoint getLat() {
		return lat;
	}

	public void setLat(LatLonPoint lat) {
		this.lat = lat;
	}
}
