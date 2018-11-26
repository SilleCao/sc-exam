package cn.sille.scexam.service;

import cn.sille.scexam.model.ExamPaper;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamPaperService {

	public ExamPaper add(ExamPaper examPaper);


	public void delete(Long id);


	public ExamPaper update(ExamPaper examPaper);


	public ExamPaper get(Long id);


	public List<ExamPaper> getAll();


	public Page<ExamPaper> getPage(Pageable pageable);

}