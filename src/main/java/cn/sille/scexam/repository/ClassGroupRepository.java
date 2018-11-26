package cn.sille.scexam.repository;

import cn.sille.scexam.model.ClassGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {
    ClassGroup findBySchoolYearAndClassCode(String schoolYear, String classCode);
}