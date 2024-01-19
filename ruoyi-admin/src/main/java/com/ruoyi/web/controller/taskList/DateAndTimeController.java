package com.ruoyi.web.controller.taskList;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.service.IDateAndTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/taskList/DateAndTime")
public class DateAndTimeController extends BaseController {
    @Autowired
    private IDateAndTimeService dateAndTimeService;

    @PostMapping("/insertDateAndTime")
    public AjaxResult insertDateAndTime(@RequestBody TaskList taskList) {
        dateAndTimeService.insertDateAndTimeToTaskList(taskList);

        return success();
    }

    @PutMapping("/updateDateAndTime")
    public AjaxResult updateDateAndTime(@RequestBody TaskList taskList) {

        dateAndTimeService.updateDateAndTimeToTaskList(taskList);

        return success();
    }

   /*
            1. 如果没有设置重复 => 只更新task_list表的日期和时间
            2. 如果有设置重复 => a. 更新task_list表的日期时间
                               b. 在template表插入任务数据
                               c. 在repeat表插入重复数据, 把b得到的template_id插入进来
                               d. 把c得到的repeat_id插入task_list表
                               e. 如果有tagId, 把b得到的template_id和tag_id插到template_tags表
        */

    public void insertTemplateProcess(TaskList taskList) {
        Long templateId = dateAndTimeService.insertTaskToTemplate(taskList);

        if(taskList.getTags().size() != 0) {
            dateAndTimeService.insertTagsToTemplateTags(taskList, templateId);
        }

        Long taskRepeatId = dateAndTimeService.insertDateAndTimeToTaskRepeat(taskList, templateId);

        dateAndTimeService.updateRepeatIdToTaskList(taskList, taskRepeatId);
    }

    @Transactional
    @PostMapping("/insertDateTimeAndRepeat")
    public AjaxResult insertDateTimeAndRepeat(@RequestBody TaskList taskList) {
        dateAndTimeService.insertDateAndTimeToTaskList(taskList);

        insertTemplateProcess(taskList);

        return success();
    }

    @Transactional
    @PutMapping("/updateDateTimeAndRepeat")
    public AjaxResult updateDateTimeAndRepeat(@RequestBody TaskList taskList) {
        dateAndTimeService.updateDateAndTimeToTaskList(taskList);

        insertTemplateProcess(taskList);

        return success();
    }

    /*
    1. 根据task_id在task_list表里更新内容
    2. 根据task_id的repeat_id在repeat表里面找到template_id
    3. 根据2的template_id, 去template表更新模版内容
     */
    @Transactional
    @PutMapping("/updateDateTimeAndRepeatForRepeat")
    public AjaxResult updateDateTimeAndRepeatForRepeat(@RequestBody TaskList taskList) {
        dateAndTimeService.updateDateAndTimeToTaskList(taskList);
        dateAndTimeService.updateDateAndTimeToTemplate(taskList);
        dateAndTimeService.updateRepeatDataToTaskRepeat(taskList);

        return success();
    }

    /*
    1. 根据repeat_id在repeat表里面找到template_id
    2. 在repeat表根据repeat_id删除repeat数据
    3. 在template表里面根据1找到的template_id删除该条数据; 如果有tag, 要根据template_id把相关tag删掉
    4. 把task_list的所有该repeat_id设为null
     */
    @DeleteMapping("/deleteRepeatForRepeat")
    @Transactional
    public AjaxResult deleteRepeatForRepeat(@RequestBody TaskList taskList) {
        dateAndTimeService.deleteTemplate(taskList);
        dateAndTimeService.deleteRepeat(taskList);
        dateAndTimeService.updateRepeatIdToNullInTaskList(taskList);

        return success();
    }
}
