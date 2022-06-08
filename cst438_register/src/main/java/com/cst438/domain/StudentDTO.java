package com.cst438.domain;

public class StudentDTO {
	public int id;
	public String studentName;
	public String studentEmail;
	public String status;
	public int statusCode;
	
	public StudentDTO() {
		this.id = 0;
		this.studentName = null;
		this.studentEmail = null;
		this.status = null;
		this.statusCode = 0;
	}
	
	public StudentDTO(String studentName, String studentEmail) {
		this.id = 0;
		this.studentName = studentName;
		this.studentEmail = studentEmail;
		this.status = null;
		this.statusCode = 0;
	}
	
	@Override
	public String toString() {
		return "StudentDTO [id=" + id + ", studentName=" + studentName + ", studentEmail=" + studentEmail
				+ ", status=" + status + ", status_code=" + statusCode +"]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentDTO other = (StudentDTO) obj;
		if (id != other.id)
			return false;
		if (studentEmail == null) {
			if (other.studentEmail != null)
				return false;
		} else if (!studentEmail.equals(other.studentEmail))
			return false;
		if (studentName == null) {
			if (other.studentName != null)
				return false;
		} else if (!studentName.equals(other.studentName))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (statusCode != other.statusCode)
			return false;
		return true;
	}
}