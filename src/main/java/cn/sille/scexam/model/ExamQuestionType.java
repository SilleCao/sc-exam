package cn.sille.scexam.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@XmlRootElement
@Entity
@Table(name = "se_exam_question_type")
public class ExamQuestionType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(name = "type_name")
	private String typeName;

    @Column(name = "target_score")
	private int targetScore;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name="exam_paper_id")
    private ExamPaper examPaper;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }

    public ExamPaper getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }
}