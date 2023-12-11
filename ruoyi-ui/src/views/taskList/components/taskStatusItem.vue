<template>
  <div :class="['circle-button', {'solid-circle': isSolidCircle, 'half-circle': isHalfCircle}]"
       @click="showDropdown">
    <ul v-show="isDropdownVisible" @click.stop class="user-select-none status-dropdown">
      <li v-for="option in options" :key="option.value"
          :value="option.value"
          class="dropdown-item"
          @click="selectOption(option)">{{ option.label }}
      </li>
    </ul>
  </div>
</template>

<script>
import {updateTaskStatus} from "../../../api/taskList/taskList";

export default {
  name: 'TaskStatusItem',
  props: {
    task: {
      type: Object,
    },
  },

  watch: {},

  data() {
    return {
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
    isHalfCircle() {
      return this.task.taskStatus === '1';
    },
    isSolidCircle() {
      return this.task.taskStatus === '2';
    },
  },

  methods: {
    showDropdown() {
      this.isDropdownVisible = !this.isDropdownVisible;
    },
    selectOption(option) {
      if (this.task.taskStatus !== option.value) {
        this.task.taskStatus = option.value;

        updateTaskStatus(this.task).then(res => {
          this.$modal.msgSuccess("任务状态修改成功");
        })
      }

      this.isDropdownVisible = false;
    },
    hideDropdown() {
      this.isDropdownVisible = false;
    }
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
  padding: 2px 4px;
  cursor: pointer;
}

.dropdown-item:hover {
  color: #1890ff;
  background-color: #F5F7FA;
}
</style>
