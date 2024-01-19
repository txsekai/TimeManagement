import request from "../../utils/request";

export function listToDoTask(query) {
  return request({
    url: '/taskList/todo/list',
    method: 'get',
    params: query
  })
}

export function addTask(data) {
  return request({
    url: '/taskList/addTask',
    method: "post",
    data: data
  })
}

export function updateTaskStatus(data) {
  return request({
    url: '/taskList/updateTaskStatus',
    method: "put",
    data: data
  })
}

export function updateTaskName(data) {
  return request({
    url: '/taskList/updateTaskName',
    method: "put",
    data: data
  })
}

export function delTask(taskId) {
  return request({
    url: '/taskList/' + taskId,
    method: "delete"
  })
}

export function insertTaskPriority(data) {
  return request({
    url: '/taskList/insertTaskPriority',
    method: "post",
    data: data
  })
}

export function updateTaskPriority(data) {
  return request({
    url: '/taskList/updateTaskPriority',
    method: "put",
    data: data
  })
}

export function deleteTemplateTaskForRepeat(data) {
  return request({
    url: '/taskList/deleteTemplateTaskForRepeat',
    method: "delete",
    data: data
  })
}
