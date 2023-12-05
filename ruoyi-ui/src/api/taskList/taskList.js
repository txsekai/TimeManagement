import request from "../../utils/request";

export function listToDoTask(query) {
  return request({
    url: '/taskList/todo/list',
    method: 'get',
    params: query
  })
}

export function delTask(taskId) {
  return request({
    url: '/taskList/' + taskId,
    method: "delete"
  })
}
