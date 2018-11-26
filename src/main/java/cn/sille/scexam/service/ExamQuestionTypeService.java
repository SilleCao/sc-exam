package cn.sille.scexam.service;

import cn.sille.scexam.model.ExamQuestionType;
import java.util.List;
import org.springframework.data.domain.Page;
public interface ExamQuestionTypeService {

	public ExamQuestionType add(ExamQuestionType examQuestionType);


	public void delete(Long id);


	public ExamQuestionType update(ExamQuestionType examQuestionType);


	public ExamQuestionType get(Long id);


	public List<ExamQuestionType> getAll();


	public Page<ExamQuestionType> getPage(Integer page, Integer rows);

}