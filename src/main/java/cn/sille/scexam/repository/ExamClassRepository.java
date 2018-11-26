package cn.sille.scexam.repository;

import cn.sille.scexam.model.ExamClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamClassRepository extends JpaRepository<ExamClass, Long> {
}