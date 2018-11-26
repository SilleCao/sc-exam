package cn.sille.scexam.service.impl;

import cn.sille.scexam.model.Student;
import cn.sille.scexam.repository.ClassGroupRepository;
import cn.sille.scexam.repository.StudentRepository;
import cn.sille.scexam.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
	@Autowired
	private StudentRepository studentRepository;
    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Override
    public Map<String, Object> getStudentStatisticData(Long id, String echartType){
        Student student = new Student();
        student.setId(id);

        

        return null;
    }
}