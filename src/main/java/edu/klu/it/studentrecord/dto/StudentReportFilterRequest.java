package edu.klu.it.studentrecord.dto;

public class StudentReportFilterRequest {

    private String filterType;     // id | name | program | course | year | cgpa

    private Long id;
    private String name;
    private String programType;
    private String course;

    private Integer yearFrom;
    private Integer yearTo;
    
    private Integer academicYear;

    public Integer getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}
	private Double cgpaMin;
    private Double cgpaMax;

    // ===== Getters & Setters =====

    public String getFilterType() {
        return filterType;
    }
    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getProgramType() {
        return programType;
    }
    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getYearFrom() {
        return yearFrom;
    }
    public void setYearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
    }

    public Integer getYearTo() {
        return yearTo;
    }
    public void setYearTo(Integer yearTo) {
        this.yearTo = yearTo;
    }

    public Double getCgpaMin() {
        return cgpaMin;
    }
    public void setCgpaMin(Double cgpaMin) {
        this.cgpaMin = cgpaMin;
    }

    public Double getCgpaMax() {
        return cgpaMax;
    }
    public void setCgpaMax(Double cgpaMax) {
        this.cgpaMax = cgpaMax;
    }
	@Override
	public String toString() {
		return "StudentReportFilterRequest [filterType=" + filterType + ", id=" + id + ", name=" + name
				+ ", programType=" + programType + ", course=" + course + ", yearFrom=" + yearFrom + ", yearTo="
				+ yearTo + ", cgpaMin=" + cgpaMin + ", cgpaMax=" + cgpaMax + "]";
	}
    
}
