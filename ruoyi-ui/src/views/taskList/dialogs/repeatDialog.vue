<template>
  <el-dialog
    :title="title"
    :visible.sync="repeatDialogVisible"
    width="30%"
    center
    append-to-body
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-row class="mb8">
        <span style="display: inline-block;padding-right: 28px;margin-right: 10px">重复</span>
        <el-select v-model="repeatValue">
          <el-option
            v-for="item in repeatOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>

          <el-option
            :key="REPEAT_SELECT.CUSTOM"
            :label="convertLabel(REPEAT_SELECT.CUSTOM)"
            :value="REPEAT_SELECT.CUSTOM"
          ></el-option>
        </el-select>
    </el-row>

    <el-row class="mb10" v-if="endRepeatVisible">
      <el-col :span="12">
        <span style="display: inline-block;margin-right: 10px">结束重复</span>
        <el-select v-model="endRepeat">
          <el-option
            v-for="item in endRepeatOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>

          <el-option
            :key="REPEAT_SELECT.ENDREPEATSELECTEDDATE"
            :label="convertLabel(REPEAT_SELECT.ENDREPEATSELECTEDDATE)"
            :value="REPEAT_SELECT.ENDREPEATSELECTEDDATE"
          ></el-option>
        </el-select>
      </el-col>

      <el-col :span="12" v-if="endDateVisible">
        <span>结束日期</span>
        <date-item style="margin-left: 10px" v-model="endRepeatDate" align="right"></date-item>
      </el-col>
    </el-row>

    <custom-repeat-item v-if="repeatValue===REPEAT_SELECT.CUSTOM"
                        :custom-result="customResult"></custom-repeat-item>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleRepeatConfirm">确认</el-button>
      <el-button @click="handleRepeatCancel">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {convertLabel, REPEAT_SELECT} from "../constants/dateSelectedConstants";
import DateItem from "../components/dateItem.vue";
import CustomRepeatItem from "../components/customRepeatItem.vue";

export default {
  name: 'RepeatDialog',
  components: {CustomRepeatItem, DateItem},
  props: {
    title: {
      type: String,
      default: '请设置重复'
    },
    repeatDialogVisible: {
      type: Boolean,
      default: false
    },
    repeatResult: {
      type: Object,
    }
  },

  data() {
    return {
      repeatValue: REPEAT_SELECT.NEVER,
      repeatOptions: [{
        value: REPEAT_SELECT.NEVER,
        label: convertLabel(REPEAT_SELECT.NEVER)
      },{
        value: REPEAT_SELECT.EVERYDAY,
        label: convertLabel(REPEAT_SELECT.EVERYDAY)
      }, {
        value: REPEAT_SELECT.EVERYWEEK,
        label: convertLabel(REPEAT_SELECT.EVERYWEEK)
      }, {
        value: REPEAT_SELECT.EVERYWORKDAY,
        label: convertLabel(REPEAT_SELECT.EVERYWORKDAY)
      }, {
        value: REPEAT_SELECT.EVERYMONTH,
        label: convertLabel(REPEAT_SELECT.EVERYMONTH)
      }, {
        value: REPEAT_SELECT.EVERYYEAR,
        label: convertLabel(REPEAT_SELECT.EVERYYEAR)
      }],
      endRepeatVisible: false,
      endRepeat: REPEAT_SELECT.NEVER,
      endRepeatOptions: [{
        value: REPEAT_SELECT.NEVER,
        label: convertLabel(REPEAT_SELECT.NEVER)
      }],
      endDateVisible: false,
      endRepeatDate: null,

      customResult: {num: null, frequencyValue: null, selectedItem: null},
      repeat: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
    }
  },

  created() {
    this.repeat = this.repeatResult;
    this.initLocalVariables(this.repeatResult);
  },

  watch: {
    repeatResult(val) {
      this.repeat = val

      this.initLocalVariables(val)
    },
    repeatValue(newVal) {
      if(newVal === REPEAT_SELECT.CUSTOM) {
        this.endRepeatVisible = true;
        this.endRepeat = this.endRepeat !== null ? this.endRepeat : REPEAT_SELECT.NEVER;
        this.customResult = Object.keys(this.customResult).length !== 0 ? this.customResult : {num: 1, frequencyValue: REPEAT_SELECT.DAY, selectedItem: []};
      }else {
        this.endRepeatVisible = newVal !== REPEAT_SELECT.NEVER
        this.endRepeat = this.endRepeat !== null ? this.endRepeat : REPEAT_SELECT.NEVER;
        this.customResult = Object.keys(this.customResult).length !== 0 ? this.customResult : {num: 1, frequencyValue: REPEAT_SELECT.DAY, selectedItem: []};
        if(newVal === REPEAT_SELECT.NEVER) {
          this.endRepeat = null;
        }
      }
    },
    endRepeat(newVal) {
      if(newVal === REPEAT_SELECT.ENDREPEATSELECTEDDATE) {
        this.endDateVisible = true;
        this.endRepeatDate = this.defaultRepeatDate();
      }else {
        this.endDateVisible = false;
        this.endRepeatDate = null;
      }
    },
  },

  computed: {
    REPEAT_SELECT() {
      return REPEAT_SELECT
    },
  },

  methods: {
    convertLabel,
    defaultRepeatDate() {
      const defaultRepeatDate = new Date();
      defaultRepeatDate.setDate(defaultRepeatDate.getDate() + 7);
      defaultRepeatDate.setHours(23,59,59,999);
      return defaultRepeatDate;
    },
    initLocalVariables(value) {
      if(value !== null) {
        // 数据库有repeat
        this.repeatValue = value.repeatValue !== null ? value.repeatValue : REPEAT_SELECT.NEVER;
        this.endRepeat = value.endRepeat !== null ? value.endRepeat : REPEAT_SELECT.NEVER;
        this.endRepeatDate = value.endRepeatDate !== null ? value.endRepeatDate : this.defaultRepeatDate();
        this.customResult = Object.keys(value.customResult).length !== 0 ? value.customResult : {num: 1, frequencyValue: REPEAT_SELECT.DAY, selectedItem: []};
      }else {
        // 新增数据
        this.repeatValue = REPEAT_SELECT.NEVER;
        this.endRepeat = REPEAT_SELECT.NEVER;
        this.endRepeatDate = this.defaultRepeatDate();
        this.customResult = {num: 1, frequencyValue: REPEAT_SELECT.DAY, selectedItem: []};
      }
    },
    handleRepeatConfirm() {
      const {repeatValue, endRepeat, endRepeatDate, customResult} = this
      this.repeat = {repeatValue, endRepeat, endRepeatDate, customResult}
      this.$emit("repeatConfirm", this.repeat);
      console.log(this.repeat)
    },
    handleRepeatCancel() {
      this.$emit("repeatCancel");
    }
  },
}
</script>

<style scoped>
.el-select {
  width: 120px;
}
</style>
