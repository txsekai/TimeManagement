const FormatList = {
  methods: {
    formattedTaskList(taskList) {
      const formatTaskList = {};

      for (let row of taskList) {
        if(row.createTimeCopy !== null) {
          let date = new Date(row.createTimeCopy);
          date.setHours(date.getHours() + 8);
          row.createTimeCopy = date.toISOString().slice(0, 10);
        }

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

        if(formatTaskList[row.createTimeCopy]) {
          // let key = row.createTimeCopy.toISOString().slice(0,10)
          // formatTaskList[key].push(row);

          formatTaskList[row.createTimeCopy].push(row);
        }else {
          formatTaskList[row.createTimeCopy] = [row];
        }
      }

      Object.keys(formatTaskList).forEach(date => {
        formatTaskList[date].sort((a, b) => {
          return new Date(a.createTimeCopy) - new Date(b.createTimeCopy);
        })
      })

      return formatTaskList;
    },
  }
}

export default FormatList
