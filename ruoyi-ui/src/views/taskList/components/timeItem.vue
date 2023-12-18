<template>
  <div class="time-select">
    <el-select class="time-time" v-model="time.hour" @change="handleChange">
      <el-option
        v-for="hour in hours"
        :key="hour"
        :label="hour"
        :value="hour"
      ></el-option>
    </el-select>
    <span> : </span>
    <el-select class="time-time" v-model="time.minute" @change="handleChange">
      <el-option
        v-for="minute in minutes"
        :key="minute"
        :label="minute"
        :value="minute"
      ></el-option>
    </el-select>
  </div>
</template>

<script>
export default {
  name: "TimeItem",
  props: {
    value: {
      type: Date,
    }
  },

  data() {
    return {
      time: {hour: 0, minute: 0},
      stateValue: null,
      hours: [],
      minutes: [],
    }
  },

  created() {
    this.initOptions();
    if(this.value !== null) {
      this.stateValue = new Date(this.value.getTime());
      this.time.hour = this.stateValue.getHours().toString().padStart(2, '0');
      this.time.minute = this.stateValue.getMinutes().toString().padStart(2, '0');
    }
  },

  watch: {
    value(val) {
      if(val !== null) {
        this.stateValue = new Date(val.getTime());
        this.time.hour = val.getHours().toString().padStart(2, '0');
        this.time.minute = val.getMinutes().toString().padStart(2, '0');
      }
    }
  },

  methods: {
    initOptions() {
      for(let i = 0; i <=23; i++) {
        const hour = i.toString().padStart(2, '0');
        this.hours.push(hour);
      }

      for(let i = 0; i <= 59; i++) {
        const minute = i.toString().padStart(2, '0');
        this.minutes.push(minute);
      }
    },
    handleChange() {
      this.stateValue.setHours(this.time.hour);
      this.stateValue.setMinutes(this.time.minute);
      this.$emit('input' ,this.stateValue);
    },
  },
}
</script>

<style scoped>
.time-select {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.time-time {
  width: 70px;
}
</style>
