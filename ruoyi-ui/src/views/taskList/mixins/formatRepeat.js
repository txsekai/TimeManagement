import DateMixin from "./formatDate";
import {convertLabel, REPEAT_SELECT} from "../constants/dateSelectedConstants";

const RepeatMixin = {
  mixins: [DateMixin],
  data() {
    return {
      labelFormatHolder: {
        selectedRepeatList: [],
        frequencyValue: '',
      }
    }
  },

  computed: {
    formattedSelectedLabel() {
      return this.formatSelectedRepeatOption(this.labelFormatHolder.frequencyValue, this.labelFormatHolder.selectedRepeatList);
    }
  },

  methods: {
    formatSelectedRepeatOption(frequencyValue, selectedRepeatList) {
      const sortedIndex = [...selectedRepeatList].sort((a, b) => a - b);
      switch (frequencyValue) {
        case REPEAT_SELECT.DAY:
          return ''
        case REPEAT_SELECT.WEEK:
          return this.formattedShowWeek(sortedIndex).map(week => `星期${week}`).join(', ')
        case REPEAT_SELECT.MONTH:
          return sortedIndex.map(day => `${day + 1}日`).join(', ')
        case REPEAT_SELECT.YEAR:
          return sortedIndex.map(month => `${month + 1}月`).join(', ')
        default:
          return null
      }
    },

    formattedShowWeek(weekOptions) {
      const weekDays = ['一', '二', '三', '四', '五', '六', '日'];
      return weekOptions.map(option => weekDays[option]);
    },

    getRepeatValueLabel(key) {
      return convertLabel(key);
    },

    formattedRepeatResult(repeat) {
      let formattedRepeat = '';

      if(repeat.repeatValue !== null) {
        formattedRepeat = `重复：${this.getRepeatValueLabel(repeat.repeatValue)}`;

        if(repeat.endRepeat !== null) {
          formattedRepeat = formattedRepeat + ` 结束重复： ${this.getRepeatValueLabel(repeat.endRepeat)}`;

          if(repeat.endRepeat == REPEAT_SELECT.ENDREPEATSELECTEDDATE) {
            formattedRepeat = `重复：${this.getRepeatValueLabel(repeat.repeatValue)} 结束重复： ${this.formatDate(repeat.endRepeatDate)}`;
          }
        }

        if(repeat.customResult.num !== null && repeat.customResult.num !== undefined) {
          formattedRepeat = formattedRepeat +
            `<br>每${repeat.customResult.num}`+
            `${repeat.customResult.frequencyValue == REPEAT_SELECT.MONTH ? '个' : ''}`+
            `${this.getRepeatValueLabel(repeat.customResult.frequencyValue)}`+
            `${this.formatSelectedRepeatOption(repeat.customResult.frequencyValue, repeat.customResult.selectedItem)}重复`;
        }
      }

      return repeat.repeatValue == null ? '' : formattedRepeat;
    }
  },
}

export default RepeatMixin
