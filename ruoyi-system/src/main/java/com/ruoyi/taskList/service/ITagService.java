package com.ruoyi.taskList.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.TaskTagParam;

import java.util.List;

public interface ITagService {
    public List<Tag> selectTagList();

    public boolean checkTagNameUnique(List<Tag> tags);

    public int insertTag(List<Tag> tags);

    public int deleteTagById(Long tagId);

    public int deleteTagInTaskTags(Long tagId);

    public int deleteTagInTemplateTags(Long tagId);

    public Long insertTaskToTaskList(TaskTagParam taskTagParam);

    public int insertTaskTags(TaskTagParam taskTagParam);

    public int delTaskTags(TaskTagParam taskTagParam);

    public List<TaskTags> selectTaskTagsList(Long taskId);

    public int insertTemplateTags(TaskTagParam taskTagParam);

    public int deleteTemplateTags(TaskTagParam taskTagParam);

    public int updateTaskNameAndPriority(TaskTagParam taskTagParam);

    public int updateTemplateTaskNameAndPriority(TaskTagParam taskTagParam);
}
