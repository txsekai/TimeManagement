import request from "../../utils/request";

export function listToDoTask(query) {
  return request({
    url: '/taskList/todo/list',
    method: 'get',
    params: query
  })
}

export function listDoingTask(query) {
  return request({
    url: '/taskList/doing/list',
    method: 'get',
    params: query
  })
}

export function listDoneTask(query) {
  return request({
    url: '/taskList/done/list',
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

export function msgInfo() {
  return request({
    url: 'taskList/getMsgInfo',
    method: 'get',
  })
}

export function addUserMsg(data) {
  return request({
    url: 'taskList/addUserMsg',
    method: 'post',
    data: data
  })
}

export function assistantProcess(data) {
  return request({
    url: 'taskList/assistantProcess',
    method: 'post',
    data: data
  })
}

export function getAssistantCatch() {
  return request({
    url: 'taskList/getAssistantCatch',
    method: 'get'
  })
}

export function handleAssistantMsgToTask(query) {
  return request({
    url: '/taskList/assistantMsgToTask',
    method: 'get',
    params: query
  })
}
