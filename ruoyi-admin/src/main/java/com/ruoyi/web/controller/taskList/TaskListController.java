package com.ruoyi.web.controller.taskList;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.service.ITaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskList")
public class TaskListController extends BaseController {

    @Autowired
    private ITaskListService taskListService;

    @GetMapping("/todo/list")
    public AjaxResult todolist(TaskListQueryParam taskListQueryParam) {
        List<TaskList> list = taskListService.selectToDoList(taskListQueryParam);
        return success(list);
    }

    @PostMapping("/addTask")
    public AjaxResult add(@Validated @RequestBody TaskList taskList) {
        return toAjax(taskListService.insertTaskList(taskList));
    }

    @PutMapping("/updateTaskStatus")
    public AjaxResult editTaskStatus(@Validated @RequestBody TaskList taskList) {
        return toAjax(taskListService.updateTaskStatus(taskList));
    }

    @PutMapping("/updateTaskName")
    public AjaxResult editTaskName(@Validated @RequestBody TaskList taskList) {
        return toAjax(taskListService.updateTaskName(taskList));
    }

    @DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds) {
        return toAjax(taskListService.deleteTaskByIds(taskIds));
    }
}
