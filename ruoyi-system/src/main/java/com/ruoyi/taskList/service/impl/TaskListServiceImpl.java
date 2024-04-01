package com.ruoyi.taskList.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.taskList.domain.entity.*;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.mapper.*;
import com.ruoyi.taskList.service.ITaskListService;
import com.ruoyi.taskList.util.UserInfoUtil;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskListServiceImpl extends ServiceImpl<TaskListMapper, TaskList> implements ITaskListService {

    @Autowired
    private TaskListMapper taskListMapper;

    @Autowired
    private TaskRepeatMapper taskRepeatMapper;

    @Autowired
    private TaskTemplateMapper taskTemplateMapper;

    @Autowired
    private TaskTagsMapper taskTagsMapper;

    @Autowired
    private TemplateTagsMapper templateTagsMapper;

    @Autowired
    private MsgInfoMapper msgInfoMapper;

    @Override
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam) {
        return taskListMapper.selectToDoList(taskListQueryParam);
    }

    @Override
    public List<TaskList> selectDoingList(TaskListQueryParam taskListQueryParam) {
        return taskListMapper.selectDoingList(taskListQueryParam);
    }

    @Override
    public List<TaskList> selectDoneList(TaskListQueryParam taskListQueryParam) {
        return taskListMapper.selectDoneList(taskListQueryParam);
    }

    @Override
    public int insertTaskList(TaskList taskList) {
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskList.setUserId(user.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);
        taskList.setCreateTimeCopy(date);
        return taskListMapper.insert(taskList);
    }

    @Override
    public int updateTaskStatus(TaskList taskList) {
        return taskListMapper.updateTaskStatus(taskList);
    }

    @Override
    public int updateTaskName(TaskList taskList) {
        return taskListMapper.updateTaskName(taskList);
    }

    @Override
    public int deleteTaskInTaskTags(Long taskId) {
        QueryWrapper<TaskTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);

        return taskTagsMapper.delete(queryWrapper);
    }

    @Override
    public int deleteTaskById(Long taskId) {
        return taskListMapper.deleteTaskById(taskId);
    }

    @Override
    public int insertTaskPriority(TaskList taskList) {
        taskList.setTaskName(taskList.getTaskName());
        taskList.setTaskStatus(taskList.getTaskStatus());
        taskList.setTaskPriority(taskList.getTaskPriority());
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskList.setUserId(user.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);
        taskList.setCreateTimeCopy(date);

        return taskListMapper.insert(taskList);
    }

    @Override
    public int updateTaskPriority(TaskList taskList) {
        taskList.setTaskId(taskList.getTaskId());
        taskList.setTaskPriority(taskList.getTaskPriority());
        taskList.setCreateTimeCopy(taskList.getCreateTime());
        Date date = new Date();
        taskList.setUpdateTime(date);
        return taskListMapper.updateById(taskList);
    }

    public Long getTemplateId(TaskList taskList) {
        Long taskRepeatId = taskList.getTaskRepeatId();

        TaskRepeat taskRepeat = taskRepeatMapper.selectById(taskRepeatId);
        Long templateId = taskRepeat.getTemplateId();

        return templateId;
    }

    @Override
    public int deleteTemplateInTemplateTags(TaskList taskList) {
        Long templateId = getTemplateId(taskList);

        QueryWrapper<TemplateTags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        return templateTagsMapper.delete(queryWrapper);
    }

    @Override
    public int deleteTaskTemplate(TaskList taskList) {
        Long templateId = getTemplateId(taskList);

        return taskTemplateMapper.deleteById(templateId);
    }

    @Override
    public int deleteTaskRepeat(TaskList taskList) {
        Long taskRepeatId = taskList.getTaskRepeatId();

        return taskRepeatMapper.deleteById(taskRepeatId);
    }

    @Override
    public int insertUserMsgToMsgInfo(MsgInfo msgInfo) {
        msgInfo.setMsgContent(msgInfo.getMsgContent());
        msgInfo.setUserId(UserInfoUtil.getUserId());
        msgInfo.setIsAssistantMsg(false);
        Date date = new Date();
        msgInfo.setCreateTime(date);

        return msgInfoMapper.insert(msgInfo);
    }

    @Override
    public List<MsgInfo> getMsgInfoList() {
        QueryWrapper<MsgInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");

        List<MsgInfo> msgInfoList = msgInfoMapper.selectList(queryWrapper);

        return msgInfoList;
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<BaseResponse<DevChatResponse>> sendUserQuestionAsync(MsgInfo msgInfo) {
        BaseResponse response = sendUserQuestion(msgInfo);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public BaseResponse<DevChatResponse> sendUserQuestion(MsgInfo msgInfo) {

        String accessKey = "lxzv6tjgcsamqmy5pycm6x33cyuuql8h";
        String secretKey = "2wh7bdciprnuay7n9ie7c8f5m7g6jslj";
        YuCongMingClient client = new YuCongMingClient(accessKey, secretKey);

        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1771479426170085377L);
        devChatRequest.setMessage(msgInfo.getMsgContent());

        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
        System.out.println(response.getData());

        return response;
    }

    @Override
    public Long insertAssistantMsgToMsgInfo(String responseData) {
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setMsgContent(responseData);
//        bug 取不到userId
//        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        msgInfo.setUserId(1L);
        msgInfo.setIsAssistantMsg(true);
        Date date = new Date();
        msgInfo.setCreateTime(date);

        msgInfoMapper.insert(msgInfo);

        return msgInfo.getMsgId();
    }
}
