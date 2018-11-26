package cn.sille.scexam.repository;

import cn.sille.scexam.model.AnswerPaperStat;
import cn.sille.scexam.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerPaperStatRepository extends JpaRepository<AnswerPaperStat,Long> {
    List<AnswerPaperStat> findAllByStudent(Student student);
}