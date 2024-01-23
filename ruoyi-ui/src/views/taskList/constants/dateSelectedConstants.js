export const REPEAT_SELECT = {
  NEVER: 'never',
  EVERYDAY: 'everyDay',
  EVERYWEEK: 'everyWeek',
  EVERYMONTH: 'everyMonth',
  EVERYYEAR: 'everyYear',
  CUSTOM: 'custom',

  ENDREPEATSELECTEDDATE: 'endRepeatSelectedDate',

  DAY: 'day',
  WEEK: 'week',
  MONTH: 'month',
  YEAR: 'year',
}

export function convertLabel(key){
  let dict = {
    never:'永不',
    everyDay:'每天',
    everyWeek: '每周',
    everyMonth: '每月',
    everyYear: '每年',
    custom: '自定义',

    endRepeatSelectedDate: '于日期',

    day: '天',
    week: '周',
    month: '月',
    year: '年',
  }
  return dict[key]
}
