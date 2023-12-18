<template>
  <el-date-picker
    class="date-item"
    v-model="stateValue"
    v-bind="$attrs"
    type="date"
    :clearable="false"
    @change="handleChange"
  ></el-date-picker>
</template>

<script>
export default {
  name: 'DateItem',
  props: {
    value: {
      type: Date,
    },
  },

  data() {
    return {
      stateValue: null,
    }
  },

  created() {
    if(this.value !== null) {
      this.stateValue = new Date(this.value.getTime());
    }
  },

  watch: {
    value(val) {
      if(val !== null) {
        this.stateValue = new Date(val.getTime());
      }
    }
  },

  methods: {
    handleChange() {
      let tmpDateTime = new Date(this.stateValue.getTime());
      tmpDateTime.setHours(this.value.getHours());
      tmpDateTime.setMinutes(this.value.getMinutes());
      // tmpDateTime.setSeconds(this.value.getSeconds());
      this.$emit('input', tmpDateTime);
    },
  },
}
</script>

<style scoped>
.date-item {
  width: 140px;
}
</style>
