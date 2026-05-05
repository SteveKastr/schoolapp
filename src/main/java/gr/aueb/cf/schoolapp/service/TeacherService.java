package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.model.static_data.Region;
import gr.aueb.cf.schoolapp.repository.RegionRepository;
import gr.aueb.cf.schoolapp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;

//    @Autowired
//    public TeacherService(TeacherRepository teacherRepository, RegionRepository regionRepository) {
//        this.teacherRepository = teacherRepository;
//        this.regionRepository = regionRepository;
//    }

    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistException.class, EntityInvalidArgumentException.class})
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO dto) throws EntityAlreadyExistException, EntityInvalidArgumentException {
        try {
            if (dto.vat() != null && teacherRepository.findByVat(dto.vat()).isPresent()) {
                throw new EntityAlreadyExistException("Teacher with vat=" +dto.vat() + " already exists.");
            }
            Region region = regionRepository.findById(dto.regionId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Region id=" + dto.regionId() + " invalid"));

            Teacher teacher = mapper.mapToTeacherEntity(dto);
            region.addTeacher(teacher);
            teacherRepository.save(teacher);
            log.info("Teacher with vat={} saved successfully", dto.vat());
            return mapper.mapToTeacherReadOnlyDTO(teacher);

        } catch (EntityAlreadyExistException e) {
            log.error("Save failed for teacher with vat={}. Teacher already exist", dto.vat(), e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Save failed for teacher with vat={}. Region id={} invalid", dto.vat(), dto.regionId());
            throw e;
        }
    }
}
