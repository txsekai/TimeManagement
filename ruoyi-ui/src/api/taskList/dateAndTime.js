import request from "../../utils/request";

export function updateDateAndTime(data) {
  return request({
    url: 'taskList/updateDateAndTime',
    method: 'put',
    data: data
  })
}
