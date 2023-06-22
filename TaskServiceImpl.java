package com.nkxgen.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nkxgen.spring.orm.dao.TaskDao;
import com.nkxgen.spring.orm.model.ResTaskFilter;
import com.nkxgen.spring.orm.model.TFilterCriteria;
import com.nkxgen.spring.orm.model.Task;
import com.nkxgen.spring.orm.model.TaskDto;

@Component
@Transactional
@Service
public class TaskServiceImpl implements TaskService {
	private final TaskDao taskDao;

	@Autowired
	public TaskServiceImpl(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	@Override
	public List<TaskDto> filterTasks(ResTaskFilter resTaskFilter) {
		return taskDao.filterTasks(resTaskFilter);
	}

	@Override
	public List<TaskDto> getTasksByUserId(int userId) {
		return taskDao.getTasksByUserId(userId);
	}

	// @Override
	// public List<TaskDto> PtfilterTasks(TFilterCriteria filterCriteria) {
	// return taskDao.taskDao(filterCriteria);
	// }

	// @Override
	// public List<Task> filterTasks(short projectFilter, String statusFilter, String categoryFilter, int userId) {
	// // TODO Auto-generated method stub
	// return taskDao.filterTasks(projectFilter, statusFilter, categoryFilter, userId);
	// }

	@Override
	public List<TaskDto> getAllTasks() {
		return taskDao.getAllTasks();
	}

	@Override

	public Task getTaskById(int taskId) {
		return taskDao.getTaskById(taskId);
	}

	@Override
	public void saveTask(Task task) {
		taskDao.saveTask(task);
	}

	@Override
	public Boolean updateStatus(int taskId) {
		// TODO Auto-generated method stub
		return taskDao.updateStatus(taskId);
	}
	// Implement other methods of the TaskService interface...

	@Override
	public List<TaskDto> PtfilterTasks(TFilterCriteria filterCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCompletedTasksByUserId(int userId) {
		return taskDao.getCompletedTasksByUserId(userId);
	}

	@Override
	public int getTotalTasksByUserId(int userId) {
		List<TaskDto> tasks = taskDao.getTasksByUserId(userId);
		return tasks.size();
	}

	@Override
	public double getHoursWorkedByUserId(int userId) {
		// Implement the logic to retrieve the hours worked by the user
		// You can use the appropriate data access method or service call here
		// and return the number of hours worked
		return taskDao.getHoursWorkedByUserId(userId);
	}

}