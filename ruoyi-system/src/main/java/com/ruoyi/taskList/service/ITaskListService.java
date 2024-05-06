package com.ruoyi.taskList.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.entity.MsgInfo;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ITaskListService extends IService<TaskList> {
    public List<TaskList> selectToDoList(TaskListQueryParam taskListQueryParam);

    public List<TaskList> selectDoingList(TaskListQueryParam taskListQueryParam);

    public List<TaskList> selectDoneList(TaskListQueryParam taskListQueryParam);

    public int insertTaskList(TaskList taskList);

    public int updateTaskStatus(TaskList taskList);

    public int updateTaskName(TaskList taskList);

    public int deleteTaskInTaskTags(Long taskId);

    public int deleteTaskById(Long taskId);

    public int insertTaskPriority(TaskList taskList);

    public int updateTaskPriority(TaskList taskList);

    public int deleteTemplateInTemplateTags(TaskList taskList);

    public int deleteTaskTemplate(TaskList taskList);

    public int deleteTaskRepeat(TaskList taskList);

    public List<MsgInfo> getMsgInfoList();

    public int insertUserMsgToMsgInfo(MsgInfo msgInfo);

    public CompletableFuture<BaseResponse<DevChatResponse>> sendUserQuestionAsync(MsgInfo msgInfo, Long assistantModuleId);

//    public BaseResponse<DevChatResponse> sendUserQuestion(MsgInfo msgInfo);

    public Long insertAssistantMsgToMsgInfo(String responseData);

    public CompletableFuture<BaseResponse<DevChatResponse>> changeMsgToJSON(MsgInfo msgInfo, Long changeMsgToJSONModuleId);

    public int insertMsgToTaskList(String result) throws JsonProcessingException;
}
