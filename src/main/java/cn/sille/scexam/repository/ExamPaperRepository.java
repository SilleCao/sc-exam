package cn.sille.scexam.repository;

import cn.sille.scexam.model.ExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExamPaperRepository extends JpaRepository<ExamPaper,Long> {
}