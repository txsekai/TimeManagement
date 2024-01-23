import request from "../../utils/request";

export function insertDateAndTime(data) {
  return request({
    url: 'taskList/DateAndTime/insertDateAndTime',
    method: 'post',
    data: data
  })
}

export function updateDateAndTime(data) {
  return request({
    url: 'taskList/DateAndTime/updateDateAndTime',
    method: 'put',
    data: data
  })
}

export function insertDateTimeAndRepeat(data) {
  return request({
    url: 'taskList/DateAndTime/insertDateTimeAndRepeat',
    method: 'post',
    data: data
  })
}

export function updateDateTimeAndRepeat(data) {
  return request({
    url: 'taskList/DateAndTime/updateDateTimeAndRepeat',
    method: 'put',
    data: data
  })
}

export function updateDateTimeAndRepeatForRepeat(data) {
  return request({

    url: 'taskList/DateAndTime/updateDateTimeAndRepeatForRepeat',
    method: 'put',
    data: data
  })
}

export function deleteRepeatForRepeat(data) {
  return request({
    url: 'taskList/DateAndTime/deleteRepeatForRepeat',
    method: 'delete',
    data: data
  })
}

export function deleteDateAndTimeAndRepeatForRepeat(data) {
  return request({
    url: 'taskList/DateAndTime/deleteDateAndTimeAndRepeatForRepeat',
    method: 'delete',
    data: data
  })
}
