package com.ruoyi.web.controller.taskList;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.service.ITaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
