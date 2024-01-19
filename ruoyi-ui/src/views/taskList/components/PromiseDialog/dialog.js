import store from "../../../../store";

export default (Vue, component) => {
  const div = document.createElement("div");
  const el = document.createElement("div");
  div.appendChild(el);
  document.body.appendChild(div);

  const ComponentConstructor = Vue.extend(component);
  return (propsData = {}, parent = undefined) => {
    let instance = new  ComponentConstructor({
      store,
      propsData,
      parent
    }).$mount(el);

    const destroyDialog = () => {
      if(instance && div.parentNode) {
        instance.$destroy();
        instance = null;
        div.parentNode && div.parentNode.removeChild(div);
      }
    };

    const closeDialog = (time) => {
      Vue.nextTick(() => {
        instance["visible"] = false;
      });
    };

    if(instance["visible"] !== undefined) {
      instance.$watch("visible", (val) => {
        if(!val) {
          setTimeout(() => destroyDialog(), 1000);
        }
      });
      Vue.nextTick(() => {
        instance["visible"] = true;
      });
    }

    const { onDone, onCancel } = propsData;
    instance.$once("done", (data) => {
      onDone && onDone(data);
      closeDialog();
    });
    instance.$once("cancel", (data) => {
      onCancel && onCancel(data);
      closeDialog();
    });
    instance.$once("lock-submit", () => {
      Vue.nextTick(() => {
        instance.$children[0]["submitLoading"] = true;
      });
    });
    instance.$once("unlock-submit", () => {
      Vue.nextTick(() => {
        instance.$children[0]["submitLoading"] = false;
      });
    });
  };
};
