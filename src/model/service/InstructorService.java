package model.service;

import model.dto.InstructorDto;

import java.util.List;

public interface InstructorService {
    void add(InstructorDto instructorDto);
    void update(InstructorDto instructorDto);
    void remove(int id);
    List<InstructorDto> searchSimilarByName(String name);
    List<InstructorDto> searchSimilarByName(String name, String sortColumn, String sortDirection);
    List<InstructorDto> searchAll();
    List<InstructorDto> searchAll(String sortColumn, String sortDirection);
}
