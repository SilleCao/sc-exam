package cn.sille.scexam.repository;

import cn.sille.scexam.model.ExamQuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExamQuestionTypeRepository extends JpaRepository<ExamQuestionType,Long> {
}