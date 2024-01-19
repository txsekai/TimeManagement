<template>
  <el-select v-model="stateValue" v-bind="$attrs" filterable @change="change" clearable>
    <el-option
      v-for="dict in options"
      :key="dict.dictValue"
      :label="dict.dictLabel"
      :value="dict.dictValue"
    />
  </el-select>
</template>

<script>
export default {
  name: 'DictSelect',
  props: {
    type: {
      type: String,
      required: true,
      default: ''
    },
    value: {
      type: [String, Array],
      default: ''
    }
  },

  data() {
    return {
      stateValue: null,
      options: []
    }
  },

  created() {
    this.getDicts(this.type).then(res => {
      this.options = res.data
    })
  },

  watch: {
    value: {
      handler(val) {
        this.stateValue = val;
      },
      immediate: true
    }
  },

  methods: {
    change(val) {
      const selectOption = this.options.find((ele) => ele.dictValue === val);
      this.$emit('input', val);
      this.$emit('change', val, {...selectOption});
    }
  }
}
</script>
