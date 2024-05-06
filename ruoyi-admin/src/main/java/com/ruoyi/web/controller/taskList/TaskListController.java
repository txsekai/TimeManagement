package com.ruoyi.web.controller.taskList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.entity.MsgInfo;
import com.ruoyi.taskList.domain.query.TaskListQueryParam;
import com.ruoyi.taskList.service.IDateAndTimeService;
import com.ruoyi.taskList.service.ITaskListService;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/taskList")
public class TaskListController extends BaseController {

    @Autowired
    private ITaskListService taskListService;

    @Autowired
    private IDateAndTimeService dateAndTimeService;

    @Autowired
    private RedisCache redisCache;

    private String msgId = "";

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

    @GetMapping("/getMsgInfo")
    public AjaxResult msgInfoList() {
        List<MsgInfo> list = taskListService.getMsgInfoList();
        return success(list);
    }

    @PostMapping("/addUserMsg")
    public AjaxResult addUserMsg(@RequestBody MsgInfo msgInfo) {
        // TODO check msg的长度

        // 1. 把用户消息存到msg_info
        taskListService.insertUserMsgToMsgInfo(msgInfo);

        return success();
    }

    @Transactional
    @PostMapping("/assistantProcess")
    public AjaxResult assistantProcess(@RequestBody MsgInfo msgInfo) {

        /*
        2. 向AI助手发请求
        3. 把助手消息存到msg_info
         */

//        BaseResponse response = taskListService.sendUserQuestion(msgInfo);
//        String responseData = (String) response.getData();
//        taskListService.insertAssistantMsgToMsgInfo(responseData);

        // 多线程+异步
//        Long assistantModuleId = 1771479426170085377L;1765000026947801089
        Long assistantModuleId = 1765000026947801089L;
        CompletableFuture<BaseResponse<DevChatResponse>> future = taskListService.sendUserQuestionAsync(msgInfo, assistantModuleId);

        future.thenAccept(baseResponse -> {
            try {
                String responseData = baseResponse.getData().getContent();
                msgId = taskListService.insertAssistantMsgToMsgInfo(responseData).toString();

                redisCache.setCacheObject(msgId, responseData, Constants.CAPTCHA_EXPIRATION, TimeUnit.HOURS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return success();
    }

    @GetMapping("/getAssistantCatch")
    public AjaxResult assistantCatch() {

        boolean hasAssistantCatch = false;

        if(msgId.length() != 0) {
            hasAssistantCatch = redisCache.hasKey(msgId);
        }

        return success(hasAssistantCatch);
    }

    @GetMapping("/assistantMsgToTask")
    public AjaxResult handleAssistantMsgToTask(MsgInfo msgInfo) throws JsonProcessingException {
        /*
        1. 让ai根据内容返回json结构
        2. 把数据插入taskList表
         */

        Long changeMsgToJSONModuleId = 1777687628838481922L;
        CompletableFuture<BaseResponse<DevChatResponse>> jsonResult = taskListService.changeMsgToJSON(msgInfo,changeMsgToJSONModuleId);
        jsonResult.thenAccept(baseResponse -> {
            try{
                // 从数据库消息中提取相关信息
                String responseData = baseResponse.getData().getContent();
                System.out.println("responseData" + responseData);

                // todo 返回的数据不能转为json应该怎么处理

                int start = responseData.indexOf('[');
                int end = responseData.indexOf(']') + 1;
                String result = responseData.substring(start, end);
                System.out.println(result);
                taskListService.insertMsgToTaskList(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        String responseData = "[{\"taskName\": \"法语考试复习\",\"taskStartTime\":\"2023-04-05 09:00:00.000\",\"taskCompletedTime\":\"2023-04-05 12:00:00.000\"},{\"taskName\": \"练习法语听力和口语\",\"taskStartTime\":\"2023-04-05 14:00:00.000\",\"taskCompletedTime\":\"2023-04-05 17:00:00.000\"}]";
//        taskListService.insertMsgToTaskList(responseData);
        return success();
    }
}
