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
      >{{ tag.tagName }}
      </el-tag>
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
import {addTags, delTag, deselectTagToTask, listTags, selectTagToTask} from "../../../api/taskList/tag";
import {v4 as uuidv4} from 'uuid';

export default {
  name: 'TagDialog',
  props: {
    tagDialogVisible: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object
    }
  },

  data() {
    return {
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      tempTags: [],
      tempSelectTagIds: [],
      tempDeselectTagIds: [],
    }
  },

  created() {
    this.getTagList();
  },

  methods: {
    getTagList() {
      listTags().then(res => {
        this.dynamicTags = res.data.map(item => ({vKey: item.tagId, tagId: item.tagId, tagName: item.tagName}));
      })
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      })
    },
    generateUniqueId() {
      return uuidv4();
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        if (this.dynamicTags.some(tag => tag.tagName === inputValue)) {
          this.$message({
            message: '标签已经存在',
            type: 'warning'
          })
        } else {
          const newTag = {
            vKey: this.generateUniqueId(),
            tagName: inputValue
          }
          this.dynamicTags.push(newTag);
          this.tempTags.push(newTag);
        }
      }

      this.inputVisible = false;
      this.inputValue = '';
    },
    handleCloseTag(tag) {
      this.$confirm("确认要删除标签吗？", "确认", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // 要close的tag在临时tempTags里面
        const tempIndex = this.tempTags.findIndex(tempTag => tempTag.vKey === tag.vKey);
        if (tempIndex !== -1) {
          this.tempTags.splice(tempIndex, 1);
          const dynamicIndex = this.dynamicTags.findIndex(dynamicTag => dynamicTag.vKey === tag.vKey);
          this.dynamicTags.splice(dynamicIndex, 1);
        } else {
          // 要close的tag在数据库里面
          delTag(tag.tagId).then(res => {
            this.$modal.msgSuccess("标签删除成功");
            this.getTagList();
          })
        }
      }).catch(() => {
      })
    },
    isSelected(tagVKey) {
      const tagIds = this.task.tags.map(tag => tag.tagId);
      return tagIds.includes(tagVKey) || this.tempSelectTagIds.includes(tagVKey);
    },
    handleSelectOrCancelTag(tag) {
      if (this.isSelected(tag.vKey)) {
        this.deselectTag(tag.vKey);
      } else {
        this.selectTag(tag.vKey);
      }
    },
    selectTag(tagVKey) {
      const tagIds = this.task.tags.map(tag => tag.tagId);
      if (!tagIds.includes(tagVKey) && !this.tempSelectTagIds.includes(tagVKey)) {
        this.tempSelectTagIds.push(tagVKey);
      }
    },
    deselectTag(tagVKey) {
      // 取消选择的tag在临时tempSelectTagIds里面
      const index = this.tempSelectTagIds.indexOf(tagVKey);
      if (index !== -1) {
        this.tempSelectTagIds.splice(index, 1);
      } else {
        // 取消选择的tag在数据库里面
        const tagIds = this.task.tags.map(tag => tag.tagId);
        if (tagIds.includes(tagVKey)) {
          this.tempDeselectTagIds.push(tagVKey);
        }
      }
    },
    handleConfirm() {
      if (this.tempTags.length > 0) {
        addTags(this.tempTags).then(() => {
          this.$modal.msgSuccess("新增标签成功");
        })


      }

      if (this.tempSelectTagIds.length > 0) {
        selectTagToTask(this.task.taskId, this.tempSelectTagIds).then(res => {
          this.$modal.msgSuccess("选择标签成功")
        })
      }

      if (this.tempDeselectTagIds.length > 0) {
        deselectTagToTask(this.task.taskId, this.tempDeselectTagIds).then(res => {
          this.$modal.msgSuccess("取消选择标签成功")
        })
      }

      this.$emit("tagConfirm")
    },
    handleClose() {
      this.$emit("tagCancel")
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
