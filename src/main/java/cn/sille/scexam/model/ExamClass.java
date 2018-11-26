package cn.sille.scexam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

@XmlRootElement
@Entity
@Table(name = "se_exam_class")
public class ExamClass implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private ExamClassStatus status;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JsonIgnore
	@JoinColumn(name="exam_paper_id")
	private ExamPaper examPaper;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonIgnore
    @JoinColumn(name="class_id")
    private ClassGroup classGroup;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

    public ExamClassStatus getStatus() {
        return status;
    }

    public void setStatus(ExamClassStatus status) {
        this.status = status;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

    public ExamPaper getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }
}