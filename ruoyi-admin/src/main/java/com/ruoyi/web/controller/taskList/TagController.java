package com.ruoyi.web.controller.taskList;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.TaskTagParam;
import com.ruoyi.taskList.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taskList/tag")
public class TagController extends BaseController {
    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public AjaxResult tagList() {
        List<Tag> list = tagService.selectTagList();

        return success(list);
    }

    @PostMapping("/addTags")
    public AjaxResult add(@Validated @RequestBody List<Tag> tags) {
        if(!tagService.checkTagNameUnique(tags)) {
            return error("标签有重复");
        }

        return toAjax(tagService.insertTag(tags));
    }

    @DeleteMapping("/{tagId}")
    @Transactional
    public AjaxResult removeTagId(@PathVariable Long tagId) {
        tagService.deleteTagById(tagId);
        tagService.deleteTagInTaskTags(tagId);
        tagService.deleteTagInTemplateTags(tagId);

        return success();
    }

    @PostMapping("/insertTaskAndSelectTagToTask")
    @Transactional
    public AjaxResult addTaskAndSelectTagToTask(@RequestBody TaskTagParam taskTagParam) {
        Long taskId = tagService.insertTaskToTaskList(taskTagParam);

        taskTagParam.getTask().setTaskId(taskId);
        tagService.insertTaskTags(taskTagParam);

        return success(taskId);
    }

    @PostMapping("/selectTagToTask")
    public AjaxResult addTagToTask(@RequestBody TaskTagParam taskTagParam) {
        return toAjax(tagService.insertTaskTags(taskTagParam));
    }

    @DeleteMapping("/deselectTagToTask")
    public AjaxResult removeTagToTask(@RequestBody TaskTagParam taskTagParam) {
        return toAjax(tagService.delTaskTags(taskTagParam));
    }

    @GetMapping("/taskTags/list")
    public AjaxResult taskTagsList(@RequestParam Long taskId) {
        List<TaskTags> taskTagsList = tagService.selectTaskTagsList(taskId);

        return success(taskTagsList);
    }

    /*
    对于有重复的任务更新除了日期, 时间和重复之外的内容
    仅更新当前任务
     */
    @PutMapping("/updateCurrentTaskForRepeat")
    @Transactional
    public AjaxResult updateCurrentTaskForRepeat(@RequestBody TaskTagParam taskTagParam) {
        tagService.updateTaskNameAndPriority(taskTagParam);
        if(taskTagParam.getHasSelectTag().equals("1")) {
            tagService.insertTaskTags(taskTagParam);
        }
        if(taskTagParam.getHasSelectTag().equals("0")) {
            tagService.delTaskTags(taskTagParam);
        }

        return success();
    }

    /*
    对于有重复的任务更新除了日期, 时间和重复之外的内容
    更新未来所有任务
     */
    @PutMapping("/updateTemplateForRepeat")
    @Transactional
    public AjaxResult updateTemplateForRepeat(@RequestBody TaskTagParam taskTagParam) {
        tagService.updateTaskNameAndPriority(taskTagParam);
        tagService.updateTemplateTaskNameAndPriority(taskTagParam);
        if(taskTagParam.getHasSelectTag().equals("1")) {
            tagService.insertTaskTags(taskTagParam);
            tagService.insertTemplateTags(taskTagParam);
        }

        if(taskTagParam.getHasSelectTag().equals("0")) {
            tagService.delTaskTags(taskTagParam);
            tagService.deleteTemplateTags(taskTagParam);
        }

        return success();
    }
}
