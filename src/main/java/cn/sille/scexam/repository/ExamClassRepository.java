package cn.sille.scexam.repository;

import cn.sille.scexam.model.ClassGroup;
import cn.sille.scexam.model.ExamClass;
import cn.sille.scexam.model.ExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamClassRepository extends JpaRepository<ExamClass, Long> {
    List<ExamClass> findAllByExamPaper(ExamPaper examPaper);
    ExamClass findByExamPaperAndClassGroup(ExamPaper examPaper, ClassGroup classGroup);
    List<ExamClass> findAllByClassGroup(ClassGroup classGroup);
}