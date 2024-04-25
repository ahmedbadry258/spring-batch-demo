package com.batch.demo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "regions")
public class Region implements Serializable {

	@Id
    @Basic
    @Column(name = "REGION_ID")
    private Long regionId;
    @Column(name = "REGION_NAME")
    private String regionName;
    @Column(name = "REGION_DESC")
    private String regionDescription;
    public Region() {
    }

    
    public Region(Long regionId, String regionName, String regionDescription) {
		super();
		this.regionId = regionId;
		this.regionName = regionName;
		this.regionDescription = regionDescription;
	}


	public Region(Long regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }

    public Long getRegionId() {
        return regionId;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    public String getRegionName() {
        return regionName;
    }
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionDescription() {
		return regionDescription;
	}

	public void setRegionDescription(String regionDescription) {
		this.regionDescription = regionDescription;
	}

	@Override
	public String toString() {
		return "Region [regionId=" + regionId + ", regionName=" + regionName + "]";
	}

}
