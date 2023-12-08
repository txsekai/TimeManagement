<template>
  <div :class="['circle-button', {'solid-circle': isSolidCircle, 'half-circle': isHalfCircle}]"
       @mouseenter="showDropdown" @mouseleave="hideDropdown">
    <ul v-show="isDropdownVisible" @click="selectOption">
      <li v-for="option in options" :key="option.value" class="dropdown-item">{{ option.label }}</li>
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
          label: 'TODO'
        },{
          value: '1',
          label: 'Doing'
        },{
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
      this.isDropdownVisible = true;
    },
    hideDropdown() {
      this.isDropdownVisible = false;
    },
    selectOption(event) {
      this.selectedValue = event.target.textContent;
    },
  }
}
</script>

<style scoped>
.circle-button {
  position: relative;
  display: inline-block;
  width: 40px;
  height: 40px;
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
  background-color: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 4px;
  z-index: 1;
  top: 30px;
}

.dropdown-item {
  padding: 2px 3px;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #e3e3e3;
}
</style>
