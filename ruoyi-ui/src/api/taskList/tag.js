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

export function selectTagToTask(taskId, tagIds) {
  const data = {
    taskId,
    tagIds
  }
  return request({
    url: 'taskList/tag/selectTagToTask',
    method: "post",
    data: data
  })
}

export function deselectTagToTask(taskId, tagIds) {
  const data = {
    taskId,
    tagIds
  }
  return request({
    url: 'taskList/tag/deselectTagToTask',
    method: "delete",
    data: data
  })
}
