package cn.sille.scexam.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@XmlRootElement
@Entity
@Table(name = "se_exam_paper")
public class ExamPaper implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "total_score")
	private Integer totalScore;

	@Column(name = "limit_time")
	private Integer limitTime;

	@Column(name = "test_date")
	private Date testDate;

	@OneToMany(mappedBy = "examPaper", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<ExamQuestionType> examQuestionTypes;

	@OneToMany(mappedBy = "examPaper", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<ExamClass> examClasses;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(Integer limitTime) {
		this.limitTime = limitTime;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

    public Set<ExamQuestionType> getExamQuestionTypes() {
		return examQuestionTypes;
	}

	public void setExamQuestionTypes(Set<ExamQuestionType> examQuestionTypes) {
		this.examQuestionTypes = examQuestionTypes;
	}

	public Set<ExamClass> getExamClasses() {
		return examClasses;
	}

	public void setExamClasses(Set<ExamClass> examClasses) {
		this.examClasses = examClasses;
	}
}