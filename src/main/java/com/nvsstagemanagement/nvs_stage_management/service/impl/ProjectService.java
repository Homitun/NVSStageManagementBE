package com.nvsstagemanagement.nvs_stage_management.service.impl;

import com.nvsstagemanagement.nvs_stage_management.dto.project.DepartmentProjectDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.project.ProjectDepartmentDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.project.ProjectMilestoneDepartmentDTO;
import com.nvsstagemanagement.nvs_stage_management.model.*;
import com.nvsstagemanagement.nvs_stage_management.repository.DepartmentProjectRepository;
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
    private final DepartmentProjectRepository departmentProjectRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    @Override
    public List<ProjectDepartmentDTO> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDepartmentDTO.class)).toList();
    }

    @Override
    public ProjectDepartmentDTO createProject(ProjectDepartmentDTO projectDTO) {
        Project createdProject = modelMapper.map(projectDTO, Project.class);
        projectRepository.save(createdProject);
        return modelMapper.map(createdProject, ProjectDepartmentDTO.class);

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
    public List<ProjectMilestoneDepartmentDTO> getAllProjectWithMilestone() {
        List<Project> projects = projectRepository.findAllWithMilestonesAndTasks();
        return projects.stream()
                .map(project -> {
                    ProjectMilestoneDepartmentDTO dto = modelMapper.map(project, ProjectMilestoneDepartmentDTO.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<ProjectDepartmentDTO> getProjectWithUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        List<Project> projects = projectRepository.findShowByUserId(userId);

        return projects.stream().map(project -> {
            ProjectDepartmentDTO dto = modelMapper.map(project, ProjectDepartmentDTO.class);
            return dto;
        }).collect(Collectors.toList());
    }
    public List<ProjectDepartmentDTO> getProjectsByDepartmentId(String departmentId) {
        List<Project> projects = departmentProjectRepository.findProjectsByDepartmentId(departmentId);
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDepartmentDTO.class))
                .collect(Collectors.toList());
    }
}
