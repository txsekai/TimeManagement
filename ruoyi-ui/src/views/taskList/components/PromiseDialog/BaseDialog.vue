<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :custom-class="`${customClass} custom-dialog`"
    v-bind="$attrs"
    :visible.sync="modalState"
    v-on="$listeners"
    @opened.once="handleOpened"
    @close="handleClose"
  >
    <slot class="base-dialog__slot"></slot>

    <div slot="footer" style="text-align: center">
      <div class="dialog-footer">
        <slot name="footerButton"></slot>
        <el-button
          v-if="showConfirm && !$slots.footerButton"
          :loading="submitLoading"
          size="small"
          type="primary"
          @click="handleConfirm"
        >{{ confirmText }}</el-button>
        <el-button
          v-if="showCancel && !$slots.footerButton"
          size="small"
          @click="handleClose"
        >{{ cancelText }}</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BaseDialog',
  props: {
    customClass: {
      type: String,
      default: ''
    },
    showConfirm: {
      type: Boolean,
      default: true
    },
    showCancel: {
      type: Boolean,
      default: true
    },
    confirmText: {
      type: String,
      default: '确定'
    },
    cancelText: {
      type: String,
      default: '关闭'
    },
    visible: {
      type: Boolean,
      default: false
    }
  },

  data() {
    return {
      modalState: false,
      submitLoading: false
    }
  },

  watch: {
    visible(val) {
      this.modalState = val;
    }
  },

  methods: {
    handleOpened() {
      this.$emit('on-opened');
    },
    handleConfirm() {
      this.$emit('on-confirm');
    },
    handleClose() {
      this.$emit('on-cancel');
    },
  },
}
</script>

<style scoped lang="scss">
::v-deep .custom-dialog {
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 30%;
  left: 40%; // v-dialogDrag会设置left位置,所以只需要做垂直居中对齐即可
  margin: 0;
  transform: translate(0, -50%);
  max-height: calc(100% - 30px);
  max-width: calc(100% - 30px);

  .el-dialog__body {
    overflow: auto;
  }
}

::v-deep .el-dialog__title {
  font-size: 16px;
  font-weight: 500;
}

::v-deep .el-dialog__header {
  padding: 16px 24px;
}

::v-deep .el-dialog__body {
  padding: 0 10px 0 10px;
  flex: 1;
}

::v-deep .el-dialog__footer {
  padding: 0;
  text-align: center;

  .dialog-footer {
    padding: 12px 10px;
  }
}

::v-deep .el-divider--horizontal {
  margin: 0;
}

::v-deep .el-input.is-disabled .el-input__inner {
  background-color: #f8f8f8;
  border-color: #ebebeb;
  color: rgba(0, 0, 0, 0.65);
  font-weight: 400;
  cursor: not-allowed;
}
</style>
