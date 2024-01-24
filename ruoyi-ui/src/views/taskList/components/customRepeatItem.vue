<template>
  <el-row>
    <el-row class="mb8">
    <span>
      日程将每{{ customResult.num }}{{
        customResult.frequencyValue == REPEAT_SELECT.MONTH ? '个' : ''
      }}{{ getFrequencyLabel }}{{ formattedSelectedLabel }}重复
    </span>
    </el-row>

    <el-row class="mb8">
      <span style="display: inline-block;margin-right: 5px">每</span>
      <el-input style="width: 70px;margin-right: 5px" v-model="customResult.num" @input="handleInput"
                @blur="handleBlur"></el-input>

      <el-select v-model="customResult.frequencyValue">
        <el-option
          v-for="item in frequencyOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </el-row>

    <el-row v-show="customResult.frequencyValue==REPEAT_SELECT.WEEK">
      <el-tag
        :effect="tagEffect(customResult.selectedItem, index)"
        class="option-button user-select-none"
        v-for="(week, index) in formattedShowWeek(weekOptions)"
        :key="index"
        @click="handleSelectOrCancel(customResult.selectedItem, index)"
      >{{ week }}
      </el-tag>
    </el-row>

    <div class="month-grid" v-show="customResult.frequencyValue==REPEAT_SELECT.MONTH">
      <el-tag :effect="tagEffect(customResult.selectedItem, month)"
              v-for="month in monthOptions"
              :key="month"
              class="option-button user-select-none"
              @click="handleSelectOrCancel(customResult.selectedItem, month)"
      >{{ month + 1 }}
      </el-tag>
    </div>

    <div class="year-grid" v-show="customResult.frequencyValue==REPEAT_SELECT.YEAR">
      <el-tag :effect="tagEffect(customResult.selectedItem, year)"
              v-for="year in yearOptions"
              :key="year"
              class="option-button user-select-none"
              @click="handleSelectOrCancel(customResult.selectedItem, year)"
      >{{ year + 1 }}
      </el-tag>
    </div>
  </el-row>
</template>

<script>
import {convertLabel, REPEAT_SELECT} from "../constants/dateSelectedConstants";
import RepeatMixin from "../mixins/formatRepeat";

export default {
  name: 'customRepeatItem',
  mixins: [RepeatMixin],

  props: {
    customResult: {
      type: Object,
    },
  },

  data() {
    return {
      frequencyOptions: [
        {
          value: REPEAT_SELECT.DAY,
          label: convertLabel(REPEAT_SELECT.DAY)
        }, {
          value: REPEAT_SELECT.WEEK,
          label: convertLabel(REPEAT_SELECT.WEEK)
        }, {
          value: REPEAT_SELECT.MONTH,
          label: convertLabel(REPEAT_SELECT.MONTH)
        }, {
          value: REPEAT_SELECT.YEAR,
          label: convertLabel(REPEAT_SELECT.YEAR)
        },
      ],
      weekOptions: [],
      monthOptions: [],
      yearOptions: [],
    }
  },

  created() {
    this.initWeekOptions();
    this.initMonthOptions();
    this.initYearOptions();
  },

  watch: {
    'customResult.frequencyValue'() {
      if (this.customResult.selectedItem !== null) {
        while (this.customResult.selectedItem.length > 0) {
          this.customResult.selectedItem.pop();
        }
      }
      this.labelFormatHolder.frequencyValue = this.customResult.frequencyValue;
      this.labelFormatHolder.selectedRepeatList = this.customResult.selectedItem;
    }
  },

  computed: {
    REPEAT_SELECT() {
      return REPEAT_SELECT
    },
    getFrequencyLabel() {
      const selectedOption = this.frequencyOptions.find(
        option => option.value === this.customResult.frequencyValue
      )
      return selectedOption ? selectedOption.label : '';
    },
    tagEffect() {
      return (selectedItem, index) => {
        if (this.isSelected(selectedItem, index)) {
          return 'dark';
        } else {
          return 'plain'
        }
      }
    },
  },

  methods: {
    initWeekOptions() {
      for (let i = 0; i <= 6; i++) {
        this.weekOptions.push(i);
      }
    },
    initMonthOptions() {
      for (let i = 0; i <= 30; i++) {
        this.monthOptions.push(i)
      }
    },
    initYearOptions() {
      for (let i = 0; i <= 11; i++) {
        this.yearOptions.push(i)
      }
    },
    handleInput() {
      this.customResult.num = this.customResult.num.replace(/[^1-9]/g, '');
    },
    handleBlur() {
      if (this.customResult.num == '') {
        this.customResult.num = 1;
      }
    },
    isSelected(selectionArray, item) {
      return selectionArray !== null ? selectionArray.includes(item) : false;
    },
    handleSelectOrCancel(selectionArray, item) {
      if (this.isSelected(selectionArray, item)) {
        this.deselect(selectionArray, item);
      } else {
        this.select(selectionArray, item)
      }
    },
    select(selectionArray, item) {
      if (selectionArray !== null) {
        selectionArray.push(item);
      } else {
        selectionArray = [item];
      }
    },
    deselect(selectionArray, item) {
      const index = selectionArray.indexOf(item);
      if (index !== -1) {
        selectionArray.splice(index, 1);
      }
    },
  },
}
</script>

<style scoped>
.option-button {
  border-radius: 2rem;
}

.el-tag {
  margin-left: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  width: 30px;
  text-align: center;
  height: 30px;
  padding: 0;
}

.month-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
}

.year-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  width: 300px;
}

.el-select {
  width: 70px;
}
</style>
