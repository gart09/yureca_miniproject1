package model.service;

import model.dto.InstructorDto;

import java.util.List;

public interface InstructorService {
    void add(InstructorDto instructorDto);
    void update(InstructorDto instructorDto);
    void remove(int id);
    List<InstructorDto> searchSimilarByName(String name);
    List<InstructorDto> searchAll();
}
