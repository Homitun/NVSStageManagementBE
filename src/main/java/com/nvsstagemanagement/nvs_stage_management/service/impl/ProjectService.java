package com.nvsstagemanagement.nvs_stage_management.service.impl;

import com.nvsstagemanagement.nvs_stage_management.dto.project.DepartmentProjectDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.project.ProjectDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.project.ProjectMilestoneDTO;
import com.nvsstagemanagement.nvs_stage_management.model.*;
import com.nvsstagemanagement.nvs_stage_management.repository.DepartmentShowRepository;
import com.nvsstagemanagement.nvs_stage_management.repository.DepartmentRepository;
import com.nvsstagemanagement.nvs_stage_management.repository.ProjectRepository;
import com.nvsstagemanagement.nvs_stage_management.repository.UserRepository;
import com.nvsstagemanagement.nvs_stage_management.service.IProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {
    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentShowRepository departmentProjectRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
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
    public List<DepartmentProjectDTO> assignDepartmentToProject(String projectID,DepartmentProjectDTO dto) {

        Project project = projectRepository.findById(projectID)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectID));

        List<DepartmentProjectDTO> resultList = new ArrayList<>();

        for (String deptID : dto.getDepartmentID()) {
            Department department = departmentRepository.findById(deptID)
                    .orElseThrow(() -> new RuntimeException("Department not found: " + deptID));

            DepartmentProjectId dpId = new DepartmentProjectId(deptID, projectID);

            if (departmentProjectRepository.existsById(dpId)) {
                throw new RuntimeException("Department " + deptID
                        + " is already assigned to project " + projectID);
            }


            DepartmentProject departmentProject = new DepartmentProject();
            departmentProject.setId(dpId);
            departmentProject.setDepartment(department);
            departmentProject.setProject(project);
            departmentProjectRepository.save(departmentProject);

            DepartmentProjectDTO responseDTO = new DepartmentProjectDTO();
            responseDTO.setDepartmentID(Collections.singletonList(deptID));
            resultList.add(responseDTO);
        }

        return resultList;
    }

    @Override
    public List<ProjectMilestoneDTO> getAllProjectWithMilestone() {
        List<Project> projects = projectRepository.findAllWithMilestonesAndTasks();
        return projects.stream()
                .map(project -> {
                    ProjectMilestoneDTO dto = modelMapper.map(project, ProjectMilestoneDTO.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<ProjectDTO> getProjectWithUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        List<Project> projects = projectRepository.findShowByUserId(userId);

        return projects.stream().map(project -> {
            ProjectDTO dto = modelMapper.map(project, ProjectDTO.class);
            dto.setDepartment(user.getDepartment().getName());
            return dto;
        }).collect(Collectors.toList());
    }
}
