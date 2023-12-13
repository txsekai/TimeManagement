package com.ruoyi.taskList.mapper;

import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.TaskTagsParam;

import java.util.List;

public interface TagMapper {
    public List<Tag> selectTagList();

    public List<Tag> checkTagNameUnique(List<String> tagName);

    public int insertTag(List<Tag> tags);

    public int deleteTagById(Long tagId);

    public int deleteTagInTaskTags(Long tagId);

    public int insertTagToTask(TaskTagsParam taskTagsParam);

    public int delTagToTask(TaskTagsParam taskTagsParam);

    public List<TaskTags> selectTaskTagsList(Long taskId);
}
