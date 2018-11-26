package cn.sille.scexam.service;

import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.Student;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

	Student add(Student student);


	void delete(Long id);


	Student update(Student student);


	Student get(Long id);


	List<Student> getAll();


	Page<Student> getPage(Pageable pageable);

    Page<ClassGroup> getClassGroupPage(Pageable pageable);

    void uploadAndSaveList(MultipartFile uploadFile, ClassGroup classGroup);

    Map<String, Object> getStudentStatisticData(Long id, String echartType);
}