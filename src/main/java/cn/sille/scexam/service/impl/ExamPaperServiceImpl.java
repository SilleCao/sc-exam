package cn.sille.scexam.service.impl;

import cn.sille.scexam.model.ExamPaper;
import cn.sille.scexam.repository.ExamQuestionTypeRepository;
import cn.sille.scexam.service.ExamPaperService;
import cn.sille.scexam.repository.ExamPaperRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ExamPaperServiceImpl implements ExamPaperService {

	@Autowired
	private ExamPaperRepository examPaperRepository;
//	@Autowired
//	private ExamQuestionTypeRepository examQuestionTypeRepository;

	@Override
	@Transactional
	public ExamPaper add(ExamPaper examPaper){
//        examQuestionTypeRepository.saveAll(examPaper.getExamQuestionTypes());
        return examPaperRepository.save(examPaper);
    }

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public void delete(Long id){
		ExamPaper examPaper = new ExamPaper();
		examPaper.setId(id);
		examPaperRepository.delete(examPaper);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ExamPaper update(ExamPaper examPaper){
		return examPaperRepository.saveAndFlush(examPaper);
	}

	@Override
    public ExamPaper get(Long id){
		return examPaperRepository.getOne(id);
	}

	@Override
    public List<ExamPaper> getAll(){
		return examPaperRepository.findAll();
	}

	@Override
    public Page<ExamPaper> getPage(Pageable pageable){
		return examPaperRepository.findAll(pageable);
	}
}