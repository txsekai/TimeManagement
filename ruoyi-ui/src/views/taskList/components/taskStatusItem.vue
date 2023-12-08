<template>
  <div :class="['circle-button', {'solid-circle': isSolidCircle, 'half-circle': isHalfCircle}]"
       @click="showDropdown">
    <ul v-show="isDropdownVisible" @click.stop class="user-select-none">
      <li v-for="option in options" :key="option.value"
          class="dropdown-item"
          @click="selectOption(option)">{{ option.label }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'TaskStatusItem',
  data() {
    return {
      selectedValue: '',
      isDropdownVisible: false,
      options: [
        {
          value: '0',
          label: 'To Do'
        }, {
          value: '1',
          label: 'Doing'
        }, {
          value: '2',
          label: 'Done'
        },
      ]
    }
  },

  computed: {
    isSolidCircle() {
      return this.selectedValue === 'Done';
    },
    isHalfCircle() {
      return this.selectedValue === 'Doing';
    }
  },

  methods: {
    showDropdown() {
      this.isDropdownVisible = !this.isDropdownVisible;
    },
    selectOption(option) {
      this.selectedValue = option.label;
      this.isDropdownVisible = false;
    },
  }
}
</script>

<style scoped>
.circle-button {
  position: relative;
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid #409EFF;
  border-radius: 50%;
  text-align: center;
  cursor: pointer;
}

.solid-circle {
  background-color: #409EFF;
}

.half-circle::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
  background-color: #409EFF;
  border-radius: 0 0 50% 50%;
}

ul {
  position: absolute;
  list-style: none;
  padding: 0;
  margin: 0;
  color: #606266;
  background-color: #fff;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  z-index: 1;
  top: 20px;
}

.dropdown-item {
  padding: 2px 3px;
  cursor: pointer;
}

.dropdown-item:hover {
  color: #1890ff;
  background-color: #F5F7FA;
}

.user-select-none {
  user-select: none;
}
</style>
