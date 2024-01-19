const FormatList = {
  methods: {
    formattedToDoList(todoList) {
      for (let row of todoList) {
        row.initialTaskName = row.taskName;
        if(row.taskStartTime !== null) {
          row.taskStartTime = new Date(row.taskStartTime);
        }
        if(row.taskCompletedTime !== null) {
          row.taskCompletedTime = new Date(row.taskCompletedTime);
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
              selectedItem: row.repeat.selectedItem!==null ? row.repeat.selectedItem.split(',').map(Number) : null
            };
          }else {
            row.repeat.customResult = {num: null, frequencyValue: null, selectedItem: null};
          }

          row.repeat = {
            repeatValue: row.repeat.repeatValue,
            endRepeat: row.repeat.endRepeat,
            endRepeatDate: row.repeat.endRepeatDate!==null ? new Date(row.repeat.endRepeatDate) : null,
            customResult: row.repeat.customResult
          };
        }
      }
      return todoList;
    },
  }
}

export default FormatList
