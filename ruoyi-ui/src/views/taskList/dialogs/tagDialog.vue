<template>
  <el-dialog
    title="请选择标签"
    :visible.sync="dialogVisible"
    width="30%"
    center
    :before-close="handleClose"
  >
    <el-row>
      <el-tag
        :effect="tagEffect(tag.tagId)"
        class="user-select-none"
        :key="tag.tagId"
        v-for="tag in dynamicTags"
        closable
        @close="handleCloseTag(tag)"
        @click="handleSelectOrCancelTag(tag)"
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
  </el-dialog>
</template>

<script>
import {addTags, delTag, deselectTagToTask, listTags, listTaskTags, selectTagToTask} from "../../../api/taskList/tag";
import {v4 as uuidv4} from 'uuid';

export default {
  name: 'TagDialog',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object
    }
  },

  data() {
    return {
      dialogVisible: false,
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

  computed: {
    tagEffect() {
      return (tagId) => {
        if(this.isSelected(tagId)) {
          return 'dark';
        }else  {
          return 'plain'
        }
      }
    },
  },

  watch: {
    value(val) {
      this.dialogVisible = val;
    }
  },

  methods: {
    getTagList() {
      listTags().then(res => {
        this.dynamicTags = res.data.map(item => ({tagId: item.tagId, tagName: item.tagName}));
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
          addTags([newTag]).then(res => {
            this.$modal.msgSuccess("新增标签成功");
            this.getTagList();
          })
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
        // 要close的tag在数据库里面
        delTag(tag.tagId).then(res => {
          this.$modal.msgSuccess("标签删除成功");
          this.getTagList();
        })
      }).catch(() => {
      })
    },
    isSelected(tagId) {
      if (this.task.tags !== undefined) {
        const tagIds = this.task.tags.map(tag => tag.tagId);

        return tagIds.includes(tagId);
      }
    },
    handleSelectOrCancelTag(tag) {
      if (this.isSelected(tag.tagId)) {
        this.deselectTag(tag.tagId);
      } else {
        this.selectTag(tag.tagId);
      }
    },
    getNewTaskTagsList() {
      listTaskTags({taskId: this.task.taskId}).then(res => {
        this.task.tags = res.data.map(tag => {
          return {tagId: tag.tagId, tagName: tag.tagData.tagName};
        })
      })
    },
    selectTag(tagId) {
      selectTagToTask(this.task.taskId, [tagId]).then(res => {
        this.$modal.msgSuccess("选择标签成功");
        this.getNewTaskTagsList();
      })
    },
    deselectTag(tagId) {
      deselectTagToTask(this.task.taskId, [tagId]).then(res => {
        this.$modal.msgSuccess("取消选择标签成功");
        this.getNewTaskTagsList();
      })
    },
    handleClose() {
      this.dialogVisible = false;
      this.$emit("input", this.dialogVisible);

      this.$parent.getToDoList();
    }
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

.el-tag {
  margin-left: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}

</style>
