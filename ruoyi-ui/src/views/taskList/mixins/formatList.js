import {listToDoTask} from "../../../api/taskList/taskList";

const FormatList = {
  methods: {
    formattedToDoList(todoList) {
      for (let row of todoList) {
        if(row.taskStartTime !== null && row.taskCompletedTime !== null) {
          row.dateAndTime = {startTime: new Date(row.taskStartTime), completedTime: new Date(row.taskCompletedTime)};
        }else {
          row.dateAndTime = {startTime: null, completedTime: null};
        }

        if (row.tags.length > 0) {
          row.tags = row.tags.map((tagRow) => {
            return {tagId: tagRow.tagId, tagName: tagRow.tagData.tagName};
          })
        }

        if(row.taskRepeatId !== null) {
          if(row.repeat.num !== null) {
            row.repeat.customResult = {
              num: row.repeat.num,
              frequencyValue: row.repeat.frequencyValue,
              selectedItem: row.repeat.selectedItem
            };
          }else {
            row.repeat.customResult = {};
          }

          row.repeat = {
            repeatValue: row.repeat.repeatValue,
            endRepeat: row.repeat.endRepeat,
            endRepeatDate: row.repeat.endRepeatDate,
            customResult: row.repeat.customResult
          };
        }
      }
      return todoList;
    },
  }
}

export default FormatList
