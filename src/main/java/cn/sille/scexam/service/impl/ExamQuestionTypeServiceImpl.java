package cn.sille.scexam.service.impl;

import cn.sille.scexam.model.ExamQuestionType;
import cn.sille.scexam.service.ExamQuestionTypeService;
import cn.sille.scexam.repository.ExamQuestionTypeRepository;
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
public class ExamQuestionTypeServiceImpl implements ExamQuestionTypeService {
	@Autowired
	private ExamQuestionTypeRepository examQuestionTypeRepository;

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ExamQuestionType add(ExamQuestionType examQuestionType){
		return examQuestionTypeRepository.save(examQuestionType);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public void delete(Long id){
		ExamQuestionType examQuestionType = new ExamQuestionType();
		examQuestionType.setId(id);
		examQuestionTypeRepository.delete(examQuestionType);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ExamQuestionType update(ExamQuestionType examQuestionType){
		return examQuestionTypeRepository.saveAndFlush(examQuestionType);
	}

	@Override
    public ExamQuestionType get(Long id){
		return examQuestionTypeRepository.getOne(id);
	}

	@Override
    public List<ExamQuestionType> getAll(){
		return examQuestionTypeRepository.findAll();
	}

	@Override
    public Page<ExamQuestionType> getPage(Integer page, Integer rows){
		Pageable pageable = PageRequest.of(page-1, rows);
		return examQuestionTypeRepository.findAll(pageable);
	}
}