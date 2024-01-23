package com.ruoyi.quartz.task;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.taskList.domain.entity.TaskList;
import com.ruoyi.taskList.domain.entity.TaskTags;
import com.ruoyi.taskList.domain.query.NeedRepeatTask;
import com.ruoyi.taskList.mapper.TaskListMapper;
import com.ruoyi.taskList.mapper.TaskRepeatMapper;
import com.ruoyi.taskList.mapper.TaskTagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component("handleRepeatTask")
public class HandleRepeatTask {
    @Autowired
    private TaskListMapper taskListMapper;

    @Autowired
    private TaskRepeatMapper taskRepeatMapper;

    @Autowired
    private TaskTagsMapper taskTagsMapper;

    /*
    1. repeat表里面找需要重复的task, repeatValue !== never &&(结束日期大于现在0点的 || endRepeat == never)
    2. 根据1找到要重复的数据的template_id去template表里面找到任务信息
    3. 把2找到的任务信息插到task表, 并把repeat_id插进去
    4. 通过2的template_id去template_tags表里面找关联的tags, 把3的新生成task_id和关联的tags插到taskTags表里面
     */
    public void repeatTaskList() {
        handleRepeat();
    }

    public List<NeedRepeatTask> needRepeat() {
        return taskRepeatMapper.findRepeatList();
    }

    @Transactional
    public void handleRepeat() {
        for (NeedRepeatTask repeatTask : needRepeat()) {
            // 要根据repeatValue来处理
            String repeatValue = repeatTask.getRepeatValue();
            Calendar today = Calendar.getInstance();
            Calendar createTime = Calendar.getInstance();
            createTime.setTime(repeatTask.getCreateTime());

            if (repeatValue.equals("everyDay")) {
                insertRepeat(repeatTask);
            } else if (repeatValue.equals("everyWeek")) {
                int repeatDayOfWeek = createTime.get(Calendar.DAY_OF_WEEK);
                int currentDayOfWeek = today.get(Calendar.DAY_OF_WEEK);

                if (currentDayOfWeek == repeatDayOfWeek) {
                    insertRepeat(repeatTask);
                }
            } else if (repeatValue.equals("everyMonth")) {
                int repeatDayOfMonth = createTime.get(Calendar.DAY_OF_MONTH);
                int currentDayOfMonth = today.get(Calendar.DAY_OF_MONTH);

                if (currentDayOfMonth == repeatDayOfMonth) {
                    insertRepeat(repeatTask);
                }
            } else if (repeatValue.equals("everyYear")) {
                int repeatMonth = createTime.get(Calendar.MONTH);
                int repeatDayOfMonth = createTime.get(Calendar.DAY_OF_MONTH);
                int currentMonth = today.get(Calendar.MONTH);
                int currentDayOfMonth = today.get(Calendar.DAY_OF_MONTH);

                if (currentDayOfMonth == repeatDayOfMonth && currentMonth == repeatMonth) {
                    insertRepeat(repeatTask);
                }
            } else if (repeatValue.equals("custom")) {
                // 这里改用LocalDate来写
                LocalDate repeatDate = repeatTask.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate currentDate = LocalDate.now();
                long num = repeatTask.getNum();
                String selectedItem = repeatTask.getSelectedItem();

                if (repeatTask.getFrequencyValue().equals("day")) {
                    long days = repeatDate.until(currentDate).getDays();

                    if(days != 0 && days % num == 0) {
                        insertRepeat(repeatTask);
                    }
                } else if (repeatTask.getFrequencyValue().equals("week")) {
                    long weeks = ChronoUnit.WEEKS.between(repeatDate, currentDate);

                    if(weeks != 0 && weeks % num == 0) {
                        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

                        // isSelected的第二个参数需要减一是因为selectedItem的index都是从0开始
                        if(isSelected(selectedItem, dayOfWeek.getValue() - 1)) {
                            insertRepeat(repeatTask);
                        }
                    }
                } else if (repeatTask.getFrequencyValue().equals("month")) {
                    Period period = Period.between(repeatDate, currentDate);
                    long months = period.toTotalMonths();

                    if(months != 0 && months % num == 0) {
                        int currentDay = currentDate.getDayOfMonth();

                        if(isSelected(selectedItem, currentDay - 1)) {
                            insertRepeat(repeatTask);
                        }
                    }
                } else if (repeatTask.getFrequencyValue().equals("year")) {
                    Period period = Period.between(repeatDate, currentDate);
                    long years = period.toTotalMonths() / 12;

                    if(years != 0 && years % num == 0) {
                        int repeatMonth = repeatDate.getMonthValue();
                        int repeatDay = repeatDate.getDayOfMonth();
                        int currentDay = currentDate.getDayOfMonth();

                        if(isSelected(selectedItem, repeatMonth - 1) && currentDay == repeatDay) {
                            insertRepeat(repeatTask);
                        }
                    }
                }
            }
        }
    }

    public static boolean isSelected(String selectedItem, int current) {
        String[] selectedList = selectedItem.split(",");
        for (String selected : selectedList) {
            if(Integer.parseInt(selected) == current) {
                return true;
            }
        }

        return false;
    }

    public void insertRepeat(NeedRepeatTask repeatTask) {
        Long taskId = insertRepeatToTaskList(repeatTask);

        if (repeatTask.getTagIds() != null) {
            insertTagsToTaskTags(taskId, repeatTask.getTagIds());
        }
    }

    public Long insertRepeatToTaskList(NeedRepeatTask repeatTask) {
        TaskList taskList = new TaskList();
        taskList.setTaskName(repeatTask.getTaskName());
        taskList.setTaskStatus(repeatTask.getTaskStatus());
        taskList.setTaskStartTime(repeatTask.getTaskStartTime());
        taskList.setTaskCompletedTime(repeatTask.getTaskCompletedTime());
        taskList.setTaskRepeatId(repeatTask.getTaskRepeatId());
        taskList.setTaskPriority(repeatTask.getTaskPriority());
        taskList.setUserId(repeatTask.getUserId());
        Date date = new Date();
        taskList.setCreateTime(date);

        taskListMapper.insert(taskList);

        return taskList.getTaskId();
    }

    public void insertTagsToTaskTags(Long taskId, String templateTagIds) {
        String[] tagIdList = templateTagIds.split(",");
        Long[] tagIds = Arrays.stream(tagIdList).map(Long::parseLong).toArray(Long[]::new);

        for (Long tagId : tagIds) {
            TaskTags taskTags = new TaskTags();

            taskTags.setTaskId(taskId);
            taskTags.setTagId(tagId);
            Date date = new Date();
            taskTags.setCreateTime(date);

            taskTagsMapper.insert(taskTags);
        }
    }
}
