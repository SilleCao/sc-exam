package cn.sille.scexam.repository;

import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByUserCodeAndAndClassGroup(String userCode, ClassGroup classGroup);
}