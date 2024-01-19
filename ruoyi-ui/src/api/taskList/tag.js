import request from "../../utils/request";

export function listTags() {
  return request({
    url: 'taskList/tag/list',
    method: 'get'
  })
}

export function addTags(data) {
  return request({
    url: 'taskList/tag/addTags',
    method: "post",
    data: data
  })
}

export function delTag(tagId) {
  return request({
    url: 'taskList/tag/' + tagId,
    method: 'delete'
  })
}

export function insertTaskAndSelectTagToTask(task, tagId) {
  const data = {
    task,
    tagId
  }
  return request({
    url: 'taskList/tag/insertTaskAndSelectTagToTask',
    method: "post",
    data: data
  })
}

export function selectTagToTask(task, tagId) {
  const data = {
    task,
    tagId
  }
  return request({
    url: 'taskList/tag/selectTagToTask',
    method: "post",
    data: data
  })
}

export function deselectTagToTask(task, tagId) {
  const data = {
    task,
    tagId
  }
  return request({
    url: 'taskList/tag/deselectTagToTask',
    method: "delete",
    data: data
  })
}

export function listTaskTags(query) {
  return request({
    url: 'taskList/tag/taskTags/list',
    method: "get",
    params: query
  })
}

export function updateCurrentTaskForRepeat(task, tagId, hasSelectTag) {
  const data = {
    task,
    tagId,
    hasSelectTag
  }
  return request({
    url: 'taskList/tag/updateCurrentTaskForRepeat',
    method: "put",
    data: data
  })
}

export function updateTemplateForRepeat(task, tagId, hasSelectTag) {
  const data = {
    task,
    tagId,
    hasSelectTag
  }
  return request({
    url: 'taskList/tag/updateTemplateForRepeat',
    method: "put",
    data: data
  })
}
