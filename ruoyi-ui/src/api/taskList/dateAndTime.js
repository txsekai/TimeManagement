import request from "../../utils/request";

export function updateDateAndTime(data) {
  return request({
    url: 'taskList/DateAndTime/update',
    method: 'put',
    data: data
  })
}
