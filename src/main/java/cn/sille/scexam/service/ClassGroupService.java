package cn.sille.scexam.service;

import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ClassGroupService {

	ClassGroup add(ClassGroup classGroup);


	void delete(Long id);


	ClassGroup update(ClassGroup classGroup);


	ClassGroup get(Long id);


	List<ClassGroup> getAll();


	Page<ClassGroup> getPage(Pageable pageable);
}