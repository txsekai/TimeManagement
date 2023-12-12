package com.ruoyi.taskList.service.impl;

import com.ruoyi.taskList.domain.entity.Tag;
import com.ruoyi.taskList.domain.query.TaskTagsParam;
import com.ruoyi.taskList.mapper.TagMapper;
import com.ruoyi.taskList.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> selectTagList() { return tagMapper.selectTagList(); }

    @Override
    public boolean checkTagNameUnique(List<Tag> tags) {
        List<String> tagName = tags.stream().map(Tag::getTagName).collect(Collectors.toList());

        List<Tag> info = tagMapper.checkTagNameUnique(tagName);

        if(!info.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public int insertTags(List<Tag> tags) {
        int rows = tagMapper.insertTags(tags);

        return rows;
    }

    @Override
    @Transactional
    public int deleteTagById(Long tagId) {
        return tagMapper.deleteTagById(tagId);
    }

    @Override
    @Transactional
    public int insertTagToTask(TaskTagsParam taskTagsParam) {
        return tagMapper.insertTagToTask(taskTagsParam);
    }

    @Override
    @Transactional
    public int delTagToTask(TaskTagsParam taskTagsParam) {
        return tagMapper.delTagToTask(taskTagsParam);
    }
}
