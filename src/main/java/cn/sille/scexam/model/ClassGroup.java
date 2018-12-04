package cn.sille.scexam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

@XmlRootElement
@Entity
@Table(name = "se_class_group")
public class ClassGroup implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "class_name")
	private String className;

    @Column(name = "school_year")
    private String schoolYear;

    @OneToMany(mappedBy = "classGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Student> students;

    @OneToMany(mappedBy = "classGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ExamClass> examClasses;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<ExamClass> getExamClasses() {
        return examClasses;
    }

    public void setExamClasses(Set<ExamClass> examClasses) {
        this.examClasses = examClasses;
    }
}