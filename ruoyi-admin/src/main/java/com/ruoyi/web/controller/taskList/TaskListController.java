package com.ruoyi.web.controller.taskList;

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
// 纯异步
//        BaseResponse response = taskListService.sendUserQuestion(msgInfo);
//        String responseData = (String) response.getData();
//        taskListService.insertAssistantMsgToMsgInfo(responseData);

        // 多线程+异步
        CompletableFuture<BaseResponse<DevChatResponse>> future = taskListService.sendUserQuestionAsync(msgInfo);

        future.thenAccept(baseResponse -> {
            try {
                String responseData = baseResponse.getData().getContent();
                msgId = taskListService.insertAssistantMsgToMsgInfo(responseData).toString();

                redisCache.setCacheObject(msgId, responseData, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return success();
    }

    @GetMapping("/getAssistantCatch")
    public AjaxResult assistantCatch() {

        // 测试code
        msgId = "123";

        boolean hasAssistantCatch = false;

        if(msgId.length() != 0) {
            hasAssistantCatch = redisCache.hasKey(msgId);
        }

        return success(hasAssistantCatch);
    }
}
