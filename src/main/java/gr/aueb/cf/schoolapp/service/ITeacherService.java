package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITeacherService {
    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    public boolean isTeacherExists(String uuid);
    Page<TeacherReadOnlyDTO> getPaginatedTeachers(Pageable pageable);
}