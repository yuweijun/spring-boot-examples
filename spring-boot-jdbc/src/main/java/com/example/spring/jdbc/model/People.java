package com.example.spring.jdbc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class People implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * GenerationType.AUTO 对于mysql不是很合适，hibernate会在数据库里新加一个表hibernate_sequence，用来保存自增id值
	 * mysql用GenerationType.IDENTITY
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String fullName;

	private String jobTitle;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Override
	public String toString() {
		return "People [id=" + id + ", fullName=" + fullName + ", jobTitle=" + jobTitle + "]";
	}

}
