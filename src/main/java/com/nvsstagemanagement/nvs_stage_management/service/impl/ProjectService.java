package com.nvsstagemanagement.nvs_stage_management.service.impl;

import com.nvsstagemanagement.nvs_stage_management.dto.project.DepartmentProjectDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.project.ProjectDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.task.TaskUserDTO;
import com.nvsstagemanagement.nvs_stage_management.model.*;
import com.nvsstagemanagement.nvs_stage_management.repository.DepartmentProjectRepository;
import com.nvsstagemanagement.nvs_stage_management.repository.DepartmentRepository;
import com.nvsstagemanagement.nvs_stage_management.repository.ProjectRepository;
import com.nvsstagemanagement.nvs_stage_management.service.IProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {
    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentProjectRepository departmentProjectRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<ProjectDTO> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDTO.class)).toList();
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project createdProject = modelMapper.map(projectDTO, Project.class);
        projectRepository.save(createdProject);
        return modelMapper.map(createdProject, ProjectDTO.class);

    }

    @Override
    public DepartmentProjectDTO assignDepartmentToProject(DepartmentProjectDTO departmentProjectDTO) {
        Department department = departmentRepository.findById(departmentProjectDTO.getDepartmentID())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Project project = projectRepository.findById(departmentProjectDTO.getProjectID())
                .orElseThrow(() -> new RuntimeException("project not found"));

        DepartmentProjectId departmentProjectId = new DepartmentProjectId(departmentProjectDTO.getDepartmentID(), departmentProjectDTO.getProjectID());

        if (departmentProjectRepository.existsById(departmentProjectId)) {
            throw new RuntimeException("Department is already assigned to this project!");
        }

        DepartmentProject departmentProject = new DepartmentProject();
        departmentProject.setId(departmentProjectId);
        departmentProject.setDepartment(department);
        departmentProject.setProject(project);

        departmentProjectRepository.save(departmentProject);

        DepartmentProjectDTO responseDTO = new DepartmentProjectDTO();
        responseDTO.setDepartmentID(departmentProjectId.getDepartmentId());
        responseDTO.setProjectID(departmentProjectDTO.getProjectID());
        return responseDTO;
    }
}
