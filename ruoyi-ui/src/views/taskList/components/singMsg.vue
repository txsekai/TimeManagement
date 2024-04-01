<template>
  <div>
    <el-row v-for="msg in msgInfoList" :key="msg.msgId">
      <el-row v-show="msg.isAssistantMsg==1" class="sing-msg assistant-msg mb20">
        <img src="../../../assets/images/plan_picture.webp" class="msg-avatar">
        <div class="show-msg-content assistant-content" style="white-space: pre-wrap;">
          {{ msg.msgContent }}
        </div>
      </el-row>

      <el-row v-show="msg.isAssistantMsg==0" class="sing-msg user-msg mb20">
        <div class="show-msg-content user-content" style="white-space: pre-wrap;">{{ msg.msgContent }}</div>
        <img :src="avatar" class="msg-avatar">
      </el-row>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: 'SingMsg',

  props: {
    msgInfoList: {
      type: Array
    }
  },

  data() {
    return {

    }
  },

  computed: {
    ...mapGetters([
      'avatar',
    ]),
  },

}
</script>

<style scoped lang="scss">
.sing-msg {
  display: flex;
  gap: 10px;
  text-align: left;
}

.assistant-msg {
  justify-content: flex-start;
}

.msg-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.user-msg {
  justify-content: flex-end;
}

.show-msg-content {
  padding: 8px;
  border-radius: 8px;
}

.assistant-content {
  background-color: rgba(0, 0, 0, 0.05);
}

.user-content {
  background-color: #e8f4ff;
  color: #1890ff;
}
</style>
