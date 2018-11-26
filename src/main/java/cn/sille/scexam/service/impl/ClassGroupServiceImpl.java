package cn.sille.scexam.service.impl;

import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.Student;
import cn.sille.scexam.repository.ClassGroupRepository;
import cn.sille.scexam.repository.StudentRepository;
import cn.sille.scexam.service.ClassGroupService;
import cn.sille.scexam.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class ClassGroupServiceImpl implements ClassGroupService {
    @Autowired
    private ClassGroupRepository classGroupRepository;

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ClassGroup add(ClassGroup classGroup){
        if (null != classGroupRepository.findBySchoolYearAndClassCode(classGroup.getSchoolYear(), classGroup.getClassCode())){
            throw new RuntimeException(classGroup.getSchoolYear()+"学年已存在班级编号"+classGroup.getClassCode());
        }
		return classGroupRepository.save(classGroup);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public void delete(Long id){
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(id);
        classGroupRepository.delete(classGroup);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public ClassGroup update(ClassGroup classGroup){
		return classGroupRepository.saveAndFlush(classGroup);
	}

	@Override
    public ClassGroup get(Long id){
		return classGroupRepository.getOne(id);
	}

	@Override
    public List<ClassGroup> getAll(){
		return classGroupRepository.findAll();
	}

	@Override
    public Page<ClassGroup> getPage(Pageable pageable){
		return classGroupRepository.findAll(pageable);
	}
}