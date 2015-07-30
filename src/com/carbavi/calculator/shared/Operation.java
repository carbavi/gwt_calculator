package com.carbavi.calculator.shared;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Operation implements Serializable {
	
	private static final long serialVersionUID = -4736944501825227581L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	@Persistent
	private Date timestamp;
	@Persistent
	private Long numberDecimal;
	@Persistent
	private String numberBinary;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Operation() {
		this.timestamp = new Date();
	}

	public Date getTimestamp() {
		if (timestamp != null) {
			return new Date(timestamp.getTime());
		} else {
			return null;
		}
	}
	
	public void setTimestamp(Date timestamp) {
		if (timestamp != null) {
			this.timestamp = new Date(timestamp.getTime());
		} else {
			this.timestamp = null;	
		}
	}
	
	public Long getNumberDecimal() {
		return numberDecimal;
	}
	
	public void setNumberDecimal(Long numberDecimal) {
		this.numberDecimal = numberDecimal;
	}
	
	public String getNumberBinary() {
		return numberBinary;
	}
	
	public void setNumberBinary(String numberBinary) {
		this.numberBinary = numberBinary;
	}

}
