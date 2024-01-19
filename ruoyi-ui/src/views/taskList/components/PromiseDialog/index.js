import OpenDialog from './dialog'
import BaseDialog from "./BaseDialog";

function install(Vue) {
  if(install.installed) return;
  install.installed = true;
  Vue.prototype.$openDialog = (comp) => OpenDialog(Vue, comp);

  Vue.mixin({
    methods: {
      $closeDialog(isDone = false, ...args) {
        this.$emit(isDone ? 'done' : 'cancel', ...args);
      },
      $lockSubmit() {
        this.$emit('lock-submit');
      },
      $unlockSubmit() {
        this.$emit('unlock-submit');
      }
    }
  })

  Vue.component('BaseDialog', BaseDialog);
}

let GlobalVue = null;
if(typeof window !== 'undefined') {
  GlobalVue = window.Vue;
}else if(typeof global !== "undefined") {
  GlobalVue = global.Vue;
}
if(GlobalVue) {
  GlobalVue.use({
    install
  })
}

export default {
  install
}
