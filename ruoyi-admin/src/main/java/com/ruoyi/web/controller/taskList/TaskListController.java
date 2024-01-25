package com.ruoyi.web.controller.taskList;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.service.IDateAndTimeService;
import com.ruoyi.taskList.service.ITaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskList")
public class TaskListController extends BaseController {

    @Autowired
    private ITaskListService taskListService;

    @Autowired
    private IDateAndTimeService dateAndTimeService;

    @GetMapping("/todo/list")
    public AjaxResult todolist(TaskListQueryParam taskListQueryParam) {
        List<TaskList> list = taskListService.selectToDoList(taskListQueryParam);
        return success(list);
    }

    @GetMapping("/doing/list")
    public AjaxResult doingList(TaskListQueryParam taskListQueryParam) {
        List<TaskList> list = taskListService.selectDoingList(taskListQueryParam);
        return success(list);
    }

    @GetMapping("/done/list")
    public AjaxResult doneList(TaskListQueryParam taskListQueryParam) {
        List<TaskList> list = taskListService.selectDoneList(taskListQueryParam);
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

    @DeleteMapping("/{taskId}")
    @Transactional
    public AjaxResult remove(@PathVariable Long taskId) {
        taskListService.deleteTaskInTaskTags(taskId);

        return toAjax(taskListService.deleteTaskById(taskId));
    }

    @PostMapping("/insertTaskPriority")
    public AjaxResult addTaskPriority(@Validated @RequestBody TaskList taskList) {
        return toAjax(taskListService.insertTaskPriority(taskList));
    }

    @PutMapping("/updateTaskPriority")
    public AjaxResult editTaskPriority(@Validated @RequestBody TaskList taskList) {
        return toAjax(taskListService.updateTaskPriority(taskList));
    }

    @DeleteMapping("/deleteTemplateTaskForRepeat")
    @Transactional
    public AjaxResult deleteTemplateTaskForRepeat(@RequestBody TaskList taskList) {
        taskListService.deleteTaskInTaskTags(taskList.getTaskId());
        taskListService.deleteTemplateInTemplateTags(taskList);
        taskListService.deleteTaskTemplate(taskList);
        dateAndTimeService.updateRepeatIdToNullInTaskList(taskList);
        taskListService.deleteTaskRepeat(taskList);
        taskListService.deleteTaskById(taskList.getTaskId());

        return success();
    }
}
