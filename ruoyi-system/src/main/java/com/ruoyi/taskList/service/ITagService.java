package com.ruoyi.taskList.service;

import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.query.TaskTagsParam;

import java.util.List;

public interface ITagService {
    public List<Tag> selectTagList();

    public boolean checkTagNameUnique(List<Tag> tags);

    public int insertTags(List<Tag> tags);

    public int deleteTagById(Long tagId);

    public int insertTagToTask(TaskTagsParam taskTagsParam);

    public int delTagToTask(TaskTagsParam taskTagsParam);
}
