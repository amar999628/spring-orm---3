package com.nkxgen.spring.orm.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nkxgen.spring.orm.model.Project;
import com.nkxgen.spring.orm.model.ResTaskFilter;
import com.nkxgen.spring.orm.model.Task;
import com.nkxgen.spring.orm.model.TaskDto;
import com.nkxgen.spring.orm.model.User;

@Repository
@Component
public class TaskDao {

	@PersistenceContext
	private EntityManager entityManager;

	public List<TaskDto> viewTasksForUser(int userId) {
		return getTasksByUserId(userId);
	}

	public Project findProjectById(Short projectId) {
		System.out.println("in findprojbyid");
		return entityManager.find(Project.class, projectId);
	}

	private User findById(int userId) {
		return entityManager.find(User.class, userId);
	}

	public List<TaskDto> getTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT t FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<Task> taskQuery = entityManager.createQuery(jpql, Task.class);
		taskQuery.setParameter("user", user);
		List<Task> tasks = taskQuery.getResultList();

		List<TaskDto> taskDTOs = new ArrayList<>();
		for (Task task : tasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			taskDTOs.add(dto);
		}

		return taskDTOs;
	}

	public List<TaskDto> getAllTasks() {
		String jpql = "SELECT t FROM Task t";// query
		TypedQuery<Task> taskQuery = entityManager.createQuery(jpql, Task.class);
		List<Task> tasks = taskQuery.getResultList();

		List<TaskDto> taskDTOs = new ArrayList<>();
		for (Task task : tasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			taskDTOs.add(dto);
		}

		return taskDTOs;
	}

	public List<TaskDto> filterTasks(ResTaskFilter resTaskFilter) {
		System.out.println("In filterTasks");

		String jpql = "SELECT t FROM Task t WHERE 1 = 1 AND t.taskSupervisor.id = :userId";

		TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);

		query.setParameter("userId", resTaskFilter.getUserId());

		if (resTaskFilter.getProject() != null && !resTaskFilter.getStatus().isEmpty()
				&& !resTaskFilter.getCategory().isEmpty()) {
			jpql = "SELECT t FROM Task t WHERE 1 = 1 AND t.taskSupervisor.id = :userId AND t.taskCategory = :category AND t.taskStatus = :status AND t.project.id = :projectId";
			Project project = findProjectById(resTaskFilter.getProject());
			query.setParameter("projectId", resTaskFilter.getProject());
		} else {
			if (resTaskFilter.getProject() != null && resTaskFilter.getProject() != 0) {
				Project project = findProjectById(resTaskFilter.getProject());
				query.setParameter("projectId", resTaskFilter.getProject());
			}

			if (resTaskFilter.getStatus() != null && !resTaskFilter.getStatus().isEmpty()) {
				query.setParameter("status", resTaskFilter.getStatus());
			}

			if (resTaskFilter.getCategory() != null && !resTaskFilter.getCategory().isEmpty()) {
				query.setParameter("category", resTaskFilter.getCategory());
			}
		}

		List<Task> filteredTasks = query.getResultList();
		List<TaskDto> filteredTaskDTOs = new ArrayList<>();

		for (Task task : filteredTasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			filteredTaskDTOs.add(dto);
		}

		return filteredTaskDTOs;
	}

	public Task getTaskById(int taskId) {
		return entityManager.find(Task.class, taskId);
	}

	public void saveTask(Task task) {
		entityManager.merge(task);
	}

	public boolean updateStatus(int taskId2) {
		String jpql = "UPDATE Task t SET t.taskStatus = :status WHERE t.taskId = :taskId";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("status", "rew");
		query.setParameter("taskId", taskId2);

		int rowsAffected = query.executeUpdate();
		return rowsAffected > 0;
	}

	public int getCompletedTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT COUNT(t) FROM Task t WHERE t.taskSupervisor = :user AND t.taskStatus = 'completed'";
		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("user", user);
		Long result = query.getSingleResult();
		return result != null ? result.intValue() : 0;
	}

	public int getTotalTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT COUNT(t) FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("user", user);
		Long result = query.getSingleResult();
		return result != null ? result.intValue() : 0;
	}

	public double getHoursWorkedByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT SUM(t.numberOfHoursRequired) FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
		query.setParameter("user", user);
		BigDecimal result = query.getSingleResult();
		return result != null ? result.doubleValue() : 0;
	}

	// Implement other methods of the TaskDao interface...
}