<template>
  <el-dialog
    title="请选择标签"
    :visible.sync="tagDialogVisible"
    width="30%"
    center
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-row>
      <el-tag
        class="user-select-none"
        :key="tag.vKey"
        v-for="tag in dynamicTags"
        closable
        @close="handleCloseTag(tag)"
        @click="handleSelectOrCancelTag(tag)"
        :class="{selected: isSelected(tag.vKey)}"
      >{{ tag.tagName }}</el-tag>
      <el-input
        class="input-new-tag"
        v-if="inputVisible"
        v-model="inputValue"
        ref="saveTagInput"
        @keyup.enter.native="handleInputConfirm"
        @blur="handleInputConfirm"
      ></el-input>
      <el-button v-else class="button-new-tag" @click="showInput">+ 新标签</el-button>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleConfirm">确认</el-button>
      <el-button @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'TagDialog',
  props: {
    tagDialogVisible: {
      type: Boolean,
      default: false
    }
  },

  data() {
    return {
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
    }
  },

  methods: {
    isSelected(tagVKey) {

    },
    handleCloseTag(tag) {

    },
    handleSelectOrCancelTag(tag) {

    },
    handleInputConfirm() {

    },
    showInput() {

    },
    handleConfirm() {

    },
    handleClose() {

    },
  },
}
</script>

<style scoped>
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  margin-bottom: 10px;
  vertical-align: bottom;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
  margin-left: 10px;
  margin-bottom: 10px;
}
</style>
