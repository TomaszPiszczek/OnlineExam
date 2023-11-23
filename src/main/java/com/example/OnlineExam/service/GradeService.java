package com.example.OnlineExam.service;

import com.example.OnlineExam.dto.grade.GradeDTO;
import com.example.OnlineExam.model.subject.Grade;
import com.example.OnlineExam.repository.subject.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeService {
    GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<GradeDTO> getUserGrades(Integer userId){
        List<Grade> grades = gradeRepository.getGradeByUserUserId(userId);
        List<GradeDTO> gradeDTOs = grades.stream()
                .map(grade -> new GradeDTO(grade.getUser().getUsername(), grade.getGrade(), grade.getTest().getTestName(), grade.getDateTime()))
                .collect(Collectors.toList());
        return gradeDTOs;
    }
}
