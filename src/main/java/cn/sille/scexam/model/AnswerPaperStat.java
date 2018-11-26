package cn.sille.scexam.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@XmlRootElement
@Entity
@Table(name = "se_answer_paper_stat")
public class AnswerPaperStat implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "total_actual_score")
	private BigDecimal totalActualScore;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JsonIgnore
	@JoinColumn(name="user_id")
	private Student student;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JsonIgnore
	@JoinColumn(name="exam_paper_id")
	private ExamPaper examPaper;

	@OneToMany(mappedBy = "answerPaperStat", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<AnswerQuestionTypeStat> answerQuestionTypeStats;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

    public BigDecimal getTotalActualScore() {
        return totalActualScore;
    }

    public void setTotalActualScore(BigDecimal totalActualScore) {
        this.totalActualScore = totalActualScore;
    }

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public ExamPaper getExamPaper() {
		return examPaper;
	}

	public void setExamPaper(ExamPaper examPaper) {
		this.examPaper = examPaper;
	}

	public Set<AnswerQuestionTypeStat> getAnswerQuestionTypeStats() {
		return answerQuestionTypeStats;
	}

	public void setAnswerQuestionTypeStats(Set<AnswerQuestionTypeStat> answerQuestionTypeStats) {
		this.answerQuestionTypeStats = answerQuestionTypeStats;
	}
}