package cn.sille.scexam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
@Entity
@Table(name = "se_answer_question_type_stat")
public class AnswerQuestionTypeStat implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(name = "actual_score")
	private BigDecimal actualScore;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name="answer_paper_stat_id")
    private AnswerPaperStat answerPaperStat;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonIgnore
    @JoinColumn(name="exam_question_type_id")
    private ExamQuestionType examQuestionType;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

    public BigDecimal getActualScore() {
        return actualScore;
    }

    public void setActualScore(BigDecimal actualScore) {
        this.actualScore = actualScore;
    }

    public AnswerPaperStat getAnswerPaperStat() {
        return answerPaperStat;
    }

    public void setAnswerPaperStat(AnswerPaperStat answerPaperStat) {
        this.answerPaperStat = answerPaperStat;
    }

    public ExamQuestionType getExamQuestionType() {
        return examQuestionType;
    }

    public void setExamQuestionType(ExamQuestionType examQuestionType) {
        this.examQuestionType = examQuestionType;
    }
}